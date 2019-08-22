import * as React from 'react';
import {Button, Col, Form, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, Row} from "reactstrap";
import axios from "axios";

interface EventAddModalProps {
    onCloseCallback?(): void,

    onAddCallback?(successful: boolean): void,
}

interface EventAddModalState {
    modalIsOpen: boolean,
    titleInput: string,
    descriptionInput: string,
    budgetInput: number,
    goalsInput: string,
    neededMaterialsInput: string,
    workPlanInput: string,
    approvedInput: boolean,
}

export default class EventAddModal extends React.Component<EventAddModalProps, EventAddModalState> {
    /* tslint:disable */
    public _isMounted = false;
    /* tslint:enable */

    public state = {
        modalIsOpen: true,
        titleInput: "",
        descriptionInput: "",
        budgetInput: 0,
        goalsInput: "",
        neededMaterialsInput: "",
        workPlanInput: "",
        approvedInput: false,
    };

    public componentDidMount() {
        this._isMounted = true
    }

    public componentWillUnmount() {
        this._isMounted = false
    }

    public render() {
        return (
            <Modal isOpen={this.state.modalIsOpen} toggle={this.closeModal}>
                <ModalHeader toggle={this.closeModal}>{"New event proposal"}</ModalHeader>
                <ModalBody>
                    <Form>
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
                    </Form>
                </ModalBody>
                <ModalFooter>
                    <Row>
                        <Col>
                            <Button onClick={this.addEventProposal} color="success">Add new proposal</Button>
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
        if (this.props.onCloseCallback) {
            this.props.onCloseCallback();
        }
    };

    private addEventProposal = () => {
        const instance = axios.create();
        instance.put(`/api/eventProposal/addEventProposal`,
            {...this.mapStateToEventProposalDTO()}
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

    private mapStateToEventProposalDTO = (): EventProposalDTO => {
        return {
            budget: this.state.budgetInput,
            description: this.state.descriptionInput,
            goals: this.state.goalsInput,
            neededMaterials: this.state.neededMaterialsInput,
            title: this.state.titleInput,
            workPlan: this.state.workPlanInput,
            approved: false,
            id: -1,
            hasAssignedReviewer: false
        };
    };
}
