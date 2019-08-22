import * as React from 'react';
import {Button, Col, Container, Form, Input, Row} from "reactstrap";
import axios from "axios";

interface NewCommentFormProps {
    id: number

    onAddedCommentCallback(success: boolean): void;
}

interface NewCommentFormState {
    commentTextInput: string;
}

interface AddNewCommentRequest {
    eventProposalId: number,
    text: string,
    url: string
}

export default class NewCommentForm extends React.Component<NewCommentFormProps, NewCommentFormState> {
    public state = {
        commentTextInput: ""
    };

    public render() {
        return (
            <Container>
                <Form onSubmit={this.handleOnSubmitComment}>
                    <Row>
                        <Col sm="10" xs="12">
                            <Input type="textarea" id="commentText" placeholder="New comment text"
                                   style={{height: '38px'}}
                                   value={this.state.commentTextInput}
                                   onChange={e => this.setState({commentTextInput: e.target.value})}/>
                        </Col>
                        <Col sm="2" xs="3">
                            <Button type="button" color="success" onClick={this.addNewComment}>Send</Button>
                        </Col>
                    </Row>
                </Form>
            </Container>
        );
    }

    private handleOnSubmitComment = (event: any) => {
        event.preventDefault();
        this.addNewComment();
    };

    private addNewComment = () => {
        const instance = axios.create();
        instance.put(`/api/comment/addNewCommentToEventProposal`, {
                eventProposalId: this.props.id,
                text: this.state.commentTextInput,
            url: `http://localhost:3000/eproposal`
            } as AddNewCommentRequest
        ).then(response => {
            this.props.onAddedCommentCallback(true);
            this.setState({commentTextInput: ""});
        })
        .catch(reason => {
            console.error(reason);
            this.props.onAddedCommentCallback(false);
        })
    }

}