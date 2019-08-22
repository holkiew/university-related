interface CommentDTO {
    id: number,
    text: string
    timestamp: string
    author: {
        firstname: string
        surname: string
        id: number
    }
}
