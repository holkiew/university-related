import * as React from 'react';
import {Button, Col, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, Row} from "reactstrap";
import axios from "axios";

interface EventUpdateModalState {
    modalIsOpen: boolean,
    titleInput: string,
    descriptionInput: string,
    budgetInput: number,
    goalsInput: string,
    neededMaterialsInput: string,
    workPlanInput: string,
}

interface EventUpdateModalProps {
    eventProposal: EventProposalDTO;

    onCloseCallback?(): void,

    onUpdateCallback?(updatedEventProposal: EventProposalDTO): void,
}

export default class EventUpdateModal extends React.Component<EventUpdateModalProps, EventUpdateModalState> {
    /* tslint:disable */
    public _isMounted = false;
    /* tslint:enable */

    constructor(props: EventUpdateModalProps) {
        super(props);
        this.state = {
            modalIsOpen: true,
            titleInput: props.eventProposal.title,
            descriptionInput: props.eventProposal.description,
            budgetInput: props.eventProposal.budget,
            goalsInput: props.eventProposal.goals,
            neededMaterialsInput: props.eventProposal.neededMaterials,
            workPlanInput: props.eventProposal.workPlan,
        }
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
                <ModalHeader toggle={this.closeModal}>{this.props.eventProposal.title}</ModalHeader>
                <ModalBody>
                    <FormGroup>
                        <FormGroup>
                            <Label for="title">Title</Label>
                            <Input type="text" id="title" placeholder="Title"
                                   value={this.state.titleInput}
                                   onChange={e => this.setState({titleInput: e.target.value})}/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="description">Description</Label>
                            <Input type="textarea" id="description" placeholder="Description"
                                   value={this.state.descriptionInput}
                                   onChange={e => this.setState({descriptionInput: e.target.value})}/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="budget">Budget</Label>
                            <Input type="number" id="budget" placeholder={"Budget"}
                                   value={this.state.budgetInput}
                                   onChange={e => this.setState({budgetInput: Number(e.target.value)})}/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="neededMaterials">Needed materials</Label>
                            <Input type="textarea" id="neededMaterials" placeholder={"Needed materials"}
                                   value={this.state.neededMaterialsInput}
                                   onChange={e => this.setState({neededMaterialsInput: e.target.value})}/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="workPlan">Work Plan</Label>
                            <Input type="textarea" id="workPlan" placeholder={"Work Plan"}
                                   value={this.state.workPlanInput}
                                   onChange={e => this.setState({workPlanInput: e.target.value})}/>
                        </FormGroup>
                    </FormGroup>
                </ModalBody>
                <ModalFooter>
                    <Row>
                        <Col>
                            <Button onClick={this.updateEventProposal} color="success">Update</Button>
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
        this.toggleModal();
        if (this.props.onCloseCallback) {
            this.props.onCloseCallback();
        }
    };

    private updateEventProposal = () => {
        console.info(this.mapStateToEventProposalDTO())
        const instance = axios.create();
        instance.patch(`/api/eventProposal/updateEventProposal`,
            {...this.mapStateToEventProposalDTO()}
        )
            .then(response => {
                if (this.props.onUpdateCallback) {
                    this.props.onUpdateCallback(this.mapStateToEventProposalDTO());
                }
                this.toggleModal();
            })
            .catch(reason => {
                this.toggleModal();
                console.error(reason);
            })
    };

    private toggleModal() {
        if (this._isMounted) {
            this.setState({modalIsOpen: !this.state.modalIsOpen});
        }
    }

    private mapStateToEventProposalDTO = (): EventProposalDTO => {
        return {
            id: this.props.eventProposal.id,
            budget: this.state.budgetInput,
            description: this.state.descriptionInput,
            goals: this.state.goalsInput,
            neededMaterials: this.state.neededMaterialsInput,
            title: this.state.titleInput,
            workPlan: this.state.workPlanInput,
            approved: false,
            hasAssignedReviewer: false
        };
    };
}
