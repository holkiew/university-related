import * as React from 'react';
import {RefObject} from 'react';
import {Button, Card, CardBody, CardHeader, CardText, CardTitle, Col, Row} from "reactstrap";
import NewCommentForm from "./NewCommentForm";
import EventCommentPanel from "./EventCommentPanel";
import axios from "axios";
import {hasRole} from "security/utils/TokenUtil";
import ROLES from "../../../security/utils/Roles";

export interface EventDetailProps {
    event: EventProposalDTO,

    onChange?(event: EventProposalDTO): void
}

interface EventDetailState {
    proposalReview: GetProposalReviewDTO
}

interface GetProposalReviewDTO {
    reviewer: {
        firstname: string,
        surname: string
    },
    reviewText: string;
    approved: boolean;
}

export default class EventDetail extends React.Component<EventDetailProps, EventDetailState> {
    private eventCommentPanelRef: RefObject<EventCommentPanel>;

    constructor(props: EventDetailProps) {
        super(props);
        this.eventCommentPanelRef = React.createRef();
    }

    public componentDidMount() {
        this.forceUpdate();
    }

    public componentDidUpdate() {
        console.info(this.props.event.hasAssignedReviewer)
    }

    public render() {
        const event = this.props.event;
        return (<Card>
            <CardHeader>
                <Row>
                    <Col xs="6">
                        <h3>Event proposal</h3>
                    </Col>
                    <Col xs="3">

                        {(!this.props.event.hasAssignedReviewer && (hasRole(ROLES.USER) || hasRole(ROLES.ADMIN))) &&
                        <Button onClick={this.assignUserToEventReview}
                                className="mr-2 float-right btn-success">Assign reviewer</Button>
                        }
                    </Col>
                    <Col xs="3">
                        {!this.props.event.approved && this.props.event.review && this.props.event.review.approved &&
                        <Button onClick={this.approveEvent}
                                className="mr-2 float-right btn-success">Approve</Button>
                        }
                    </Col>
                </Row>
            </CardHeader>
            <CardBody>
                <Row>
                    <Col>
                        <CardTitle>{event.title}</CardTitle>
                        <CardText>Description: {event.description}</CardText>
                        <CardText>Goals: {event.goals}</CardText>
                        <CardText>WorkPlan: {event.workPlan}</CardText>
                        <CardText>Materials: {event.neededMaterials}</CardText>
                        <CardText>Budget: ${event.budget}</CardText>
                        <CardText>Approved: {event.approved ? "Yes" : "No"}</CardText>
                        {event.review && (
                            <div><CardText>Revieved
                                by: {event.review.reviewer.firstname} {event.review.reviewer.surname}</CardText>
                                <CardText>Review status: {event.review.approved ? "Acceepted" : "Rejected"}</CardText>
                                <CardText>Comment: {event.review.reviewText}</CardText>
                            </div>)
                        }
                    </Col>
                </Row>
                <hr/>
                {!event.approved &&
                <Row>
                    <NewCommentForm id={event.id}
                                    onAddedCommentCallback={() => this.forceUpdate()}/>
                </Row>
                }
                <hr/>
                <Row style={{maxHeight: '300px', overflowY: 'scroll'}}>
                    <EventCommentPanel ref={this.eventCommentPanelRef}
                                       proposalId={event.id}/>
                </Row>
            </CardBody>
        </Card>);
    }

    private approveEvent = () => {
        const proposalId = this.props.event.id;
        const instance = axios.create();
        instance.post(`/api/eventProposal/approveProposal`, {},
            {
                params: {
                    proposalId
                }
            }
        )
            .then(response => {
                if (this.props.onChange !== undefined) {
                    this.props.onChange(this.props.event);
                }
            })
            .catch(reason => {
                console.error(reason);
            })
    };

    private assignUserToEventReview = () => {
        const proposalId = this.props.event.id;
        const instance = axios.create();
        instance.post(`/api/eventProposalReview/${hasRole(ROLES.USER) ? 'assignNewProposalReviewer' : 'assignNewProposalReviewerByAdmin'}`, {},
            {
                params: {
                    proposalId
                }
            }
        )
            .then(response => {
                if (this.props.onChange !== undefined) {
                    this.props.onChange(this.props.event);
                }
            })
            .catch(reason => {
                console.error(reason);
            })
    };

    private getProposalReview = () => {
        const proposalId = this.props.event.id;
        const instance = axios.create();
        instance.get(`/api/eventProposalReview/getProposalReview`, {
                params: {
                    proposalId
                }
            },
        )
            .then(response => {
                this.setState({
                    proposalReview: response.data
                });
            })
            .catch(reason => {
                console.error(reason);
            })
    };
}
