import * as React from 'react';
import {RefObject} from 'react';
import axios from "axios";
import {Button, Col, Row} from 'reactstrap';
import EventUpdateModal from "./components/EventUpdateModal";
import EventDeleteModal from "./components/EventDeleteModal";
import EventAddModal from "./components/EventAddModal";
import EventList, {PageData} from "./components/EventList";
import EventDetail from "./components/EventDetail";

interface EventProposalState {
    pageData: PageData
    pageSize: number
    pageIndex: number
    loading: boolean
    clickedRow: EventProposalDTO
    modalType: MODAL_TYPE
}

enum MODAL_TYPE {
    CLOSED,
    UPDATE_EVENT,
    ADD_EVENT,
    DELETE_EVENT
}

export default class EventProposalView extends React.Component<any, EventProposalState> {
    private readonly eventListRef: RefObject<EventList>;
    constructor(props: any) {
        super(props);
        this.state = {
            pageData: {
                proposalDTOList: [],
                totalPages: -1
            },
            loading: true,
            pageSize: 5,
            pageIndex: 0,
            clickedRow: {} as EventProposalDTO,
            modalType: MODAL_TYPE.CLOSED,
        };
        this.eventListRef = React.createRef();
    }

    public render() {
        return (
            <div className="mt-3">
                {this.showModal()}
                <Row className="col-12">
                    <Col xs="6">
                        <h1>Event propsals</h1>
                    </Col>
                    <Col xs="6">
                        <Button onClick={this.showAddEventProposalModal} className="mr-2 float-right btn-success">New proposal</Button>
                    </Col>
                </Row>
                <hr />
                <Row className="col-12">
                    <Col sm="6" xs="12">
                        <EventList ref={this.eventListRef} {...this.state}
                                   onRowClick={this.eventSelectedHandler}
                                   fetchData={this.fetchEventProposals}
                                   showDeleteEventProposalModal={this.showDeleteEventProposalModal}
                                   showUpdateEventProposalModal={this.showUpdateEventProposalModal} />
                    </Col>
                    <Col sm="6" xs="12">
                        {this.state.clickedRow.id !== undefined &&
                            <EventDetail event={this.state.clickedRow} onChange={this.detailChangeHandler} />
                        }
                    </Col>
                </Row>
            </div>
        );
    }
    private detailChangeHandler = (detail: EventProposalDTO) => {
        this.setState({ clickedRow: detail });
    };

    private showModal = () => {
        switch (this.state.modalType) {
            case MODAL_TYPE.ADD_EVENT:
                return <EventAddModal onAddCallback={this.onModalDataUpdate}
                                      onCloseCallback={this.onCloseModal}/>;
            case MODAL_TYPE.DELETE_EVENT:
                return <EventDeleteModal onDeleteCallback={this.onModalDataUpdate}
                                         eventProposal={this.state.clickedRow} onCloseCallback={this.onCloseModal}/>;
            case MODAL_TYPE.UPDATE_EVENT:
                return <EventUpdateModal eventProposal={this.state.clickedRow} onCloseCallback={this.onCloseModal}
                                         onUpdateCallback={this.onModalDataUpdate}/>;
            default:
                return "";
        }
    };

    private onCloseModal = () => {
        this.setState({modalType: MODAL_TYPE.CLOSED})
    };

    private onModalDataUpdate = (eventProposal?: EventProposalDTO | boolean) => {
        this.fetchEventProposals(this.state.pageIndex, this.state.pageSize)
        this.onCloseModal();
    };

    private fetchEventProposals = (page?: number, pageSize?: number) => {
        this.setState({
            loading: true,
        });
        const instance = axios.create();
        instance.get(`/api/eventProposal/getAllProposals`, {
            params: {
                page: page !== undefined ? page : this.state.pageIndex,
                pageSize: pageSize !== undefined ? pageSize : this.state.pageSize,
            }
        })
            .then(response => {
                this.setState({
                    pageData: response.data,
                    loading: false
                })
            })
            .catch(reason => {
                console.error(reason);
            })
    };

    private eventSelectedHandler = (rowIndex: number) => {
        this.setState({clickedRow: this.state.pageData.proposalDTOList && this.state.pageData.proposalDTOList[rowIndex]});
    };

    private showUpdateEventProposalModal = (eventProposal: EventProposalDTO) => {
        this.setState({modalType: MODAL_TYPE.UPDATE_EVENT, clickedRow: eventProposal});
    };

    private showAddEventProposalModal = () => {
        this.setState({modalType: MODAL_TYPE.ADD_EVENT});
    };

    private showDeleteEventProposalModal = (eventProposal: EventProposalDTO) => {
        this.setState({modalType: MODAL_TYPE.DELETE_EVENT, clickedRow: eventProposal});
    };
}