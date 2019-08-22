import * as React from 'react';
import {Button, Col, Modal, ModalBody, ModalFooter, ModalHeader, Row} from "reactstrap";
import axios from "axios";

interface EventDeleteModalProps {
    eventProposal: EventProposalDTO;

    onCloseCallback?(): void,

    onDeleteCallback?(successful: boolean): void,
}

interface EventDeleteModalState {
    modalIsOpen: boolean
}

export default class EventDeleteModal extends React.Component<EventDeleteModalProps, EventDeleteModalState> {
    /* tslint:disable */
    public _isMounted = false;
    /* tslint:enable */
    public state = {
        modalIsOpen: true
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
                <ModalHeader toggle={this.closeModal}>{this.props.eventProposal.title}</ModalHeader>
                <ModalBody>
                    {`Do you really want to delete ${this.props.eventProposal.title} event proposal?`}
                </ModalBody>
                <ModalFooter>
                    <Row>
                        <Col>
                            <Button onClick={this.deleteEventProposal} color="danger">Delete</Button>
                        </Col>
                        <Col>
                            <Button onClick={this.closeModal} color="success">Cancel</Button>
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

    private deleteEventProposal = () => {
        const instance = axios.create();
        instance.delete(`/api/eventProposal/deleteEventProposal`,
            {
                params: {
                    eventProposalId: this.props.eventProposal.id
                }
            }
        )
            .then(response => {
                if (this.props.onDeleteCallback) {
                    this.props.onDeleteCallback(true);
                }
                this.toggleModal();
            })
            .catch(reason => {
                if (this.props.onDeleteCallback) {
                    this.props.onDeleteCallback(false);
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
}
