import * as React from 'react';
import {Button, Col, Form, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, Row} from "reactstrap";
import axios from "axios";
import {EventProposalReviewDTO} from "./interfaces/EventProposalReviewDTO";

interface ReviewAddModalProps {
    eventProposalId: number
    onAddCallback(successful: boolean): void
    onCancel(): void
}

interface ReviewAddModalState {
    eventProposalId: number
    modalIsOpen: boolean
    reviewTextInput: string
    approvedInput: boolean
}

export default class ReviewAddModal extends React.Component<ReviewAddModalProps, ReviewAddModalState> {
    /* tslint:disable */
    public _isMounted = false;
    /* tslint:enable */

    constructor (props: ReviewAddModalProps) {
        super(props);
        this.state = {
            eventProposalId: props.eventProposalId,
            modalIsOpen: true,
            reviewTextInput: "",
            approvedInput: true
        };
    }

    public componentDidMount() {
        this._isMounted = true
    }

    public componentWillUnmount() {
        this._isMounted = false
    }

    public render() {
        return (
            <Modal isOpen={this.state.modalIsOpen} toggle={this.closeModal}>
                <ModalHeader toggle={this.closeModal}>{"Add Review"}</ModalHeader>
                <ModalBody>
                    <Form>
                        <FormGroup>
                            <Label for="reviewText">Review</Label>
                            <Input type="textarea" id="reviewText" placeholder="review text"
                                   value={this.state.reviewTextInput}
                                   onChange={e => this.setState({reviewTextInput: e.target.value})}/>
                        </FormGroup>
                        <FormGroup tag="fieldset" row>
                            <Col sm={10}>
                                <FormGroup check>
                                    <Input type="checkbox" name="approved" id="approved"
                                           onChange={(e) => this.setState({approvedInput: e.target.checked})}/>
                                    <Label for="approved" check>Approved</Label>
                                </FormGroup>
                            </Col>
                        </FormGroup>
                    </Form>
                </ModalBody>
                <ModalFooter>
                    <Row>
                        <Col>
                            <Button onClick={this.addEventProposalReviewProposal} color="success">Submit</Button>
                        </Col>
                        <Col>
                            <Button onClick={this.closeModal} color="danger">Cancel</Button>
                        </Col>
                    </Row>
                </ModalFooter>
            </Modal>
        );
    }

    private closeModal = () => {
        this.setState({modalIsOpen: !this.state.modalIsOpen});
        this.props.onCancel();
    };

    private addEventProposalReviewProposal = () => {
        const instance = axios.create();
        instance.put(`/api/eventProposalReview/addEventProposalReview`,
            {...this.mapStateToEventProposalReviewDTO()}
        )
            .then(response => {
                if (this.props.onAddCallback) {
                    this.props.onAddCallback(true);
                }
                this.toggleModal();
            })
            .catch(reason => {
                if (this.props.onAddCallback) {
                    this.props.onAddCallback(false);
                }
                this.toggleModal();
                console.error(reason);
            })
    };

    private toggleModal() {
        if (this._isMounted) {
            this.setState({modalIsOpen: !this.state.modalIsOpen});
        }
    }

    private mapStateToEventProposalReviewDTO = (): EventProposalReviewDTO => {
        return {
            eventProposalId: this.state.eventProposalId,
            reviewText: this.state.reviewTextInput,
            approved: this.state.approvedInput,
        };
    };
}
