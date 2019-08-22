import * as React from 'react';
import {Col, Container, Jumbotron, Row} from 'reactstrap';
// @ts-ignore(no types for this library)
import Timestamp from "react-timestamp";
import {MdClose} from "react-icons/md";
import {decodedToken, TokenDTO} from "security/utils/TokenUtil";
import axios from "axios";

interface EventCommentPanelProps {
    proposalId: number
}

interface EventCommentPanelState {
    comments: CommentDTO[]
}

interface DeleteCommentRequest {
    commentId: number;
    eventProposalId: number;
}

export default class EventCommentPanel extends React.Component<EventCommentPanelProps, EventCommentPanelState> {
    public state = {
        comments: []
    };
    public canFetchData: boolean = false;


    public componentWillReceiveProps() {
        this.canFetchData = true;
    }

    public componentDidUpdate() {
        if (this.props.proposalId !== undefined && this.canFetchData) {
            this.fetchComments();
            this.canFetchData = false;
        }
    }

    public render() {
        return (
            this.state.comments.map((comment: CommentDTO, index: number) => {
                return (
                    <Jumbotron className="container" style={{padding: ".2rem", marginTop: ".5rem", marginBottom: "0rem"}} key={index}>
                        <Container>
                            <Row>
                                <Col sm={{size: 2}} md={{size: 2}}>
                                    <h5>{`${comment.author.firstname} ${comment.author.surname}`}</h5>
                                </Col>
                                <Col sm={{size: 3, offset: 7}} md={{size: 3, offset: 7}}>
                                    <Row>
                                        <Col sm="10" md="10">
                                            <Timestamp time={comment.timestamp} precision={1}/>
                                        </Col>
                                        <Col sm="2" md="2">
                                            {this.isCommentAuthorCurrentLoggedUser(comment) ?
                                                <MdClose style={{color: "red"}}
                                                         onClick={() => this.deleteComment(comment)}/> : ""}
                                        </Col>
                                    </Row>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <p>{comment.text}</p>
                                </Col>
                            </Row>
                        </Container>
                    </Jumbotron>
                );
            })
        )
    }

    private isCommentAuthorCurrentLoggedUser = (comment: CommentDTO) => {
        const token: TokenDTO | null = decodedToken();
        if (token) {
            return token.sub === String(comment.author.id)
        }
        return false;
    };

    private fetchComments = () => {
        const {proposalId} = this.props;
        const instance = axios.create();
        instance.get(`/api/comment/getAllEventProposalComments`, {
            params: {
                proposalId
            }
        })
            .then(response => {
                this.setState({
                    comments: response.data,
                })
            })
            .catch(reason => {
                console.error(reason);
            })
    };

    private deleteComment = (comment: CommentDTO) => {
        const instance = axios.create();
        instance.delete(`/api/comment/deleteEventProposalComment`, {
            data: {
                commentId: comment.id,
                eventProposalId: this.props.proposalId
            } as DeleteCommentRequest
        })
            .then(response => {
                this.fetchComments();
            })
            .catch(reason => {
                console.error(reason);
            })
    };
}
