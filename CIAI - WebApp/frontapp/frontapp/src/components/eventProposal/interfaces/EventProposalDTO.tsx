interface EventProposalDTO {
    budget: number
    description: string
    goals: string
    id: number
    neededMaterials: string
    title: string
    workPlan: string
    approved: boolean
    hasAssignedReviewer: boolean,
    review?: {
        approved: boolean,
        reviewText: string,
        reviewer: {
            firstname: string,
            surname: string
        }
    }
}