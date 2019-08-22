package fct.ciai.general.ecma.eventproposal.service

import fct.ciai.general.ecma.eventproposal.controller.payload.request.AddEventProposalRequest
import fct.ciai.general.ecma.persistence.model.Company
import fct.ciai.general.ecma.persistence.model.EventProposal
import fct.ciai.general.ecma.persistence.model.User
import fct.ciai.general.ecma.persistence.repository.EventProposalRepository
import fct.ciai.general.security.model.AuthUser
import fct.ciai.general.security.model.Role
import fct.ciai.general.security.model.RoleName
import spock.lang.Ignore
import spock.lang.Specification

class EventProposalServiceTest extends Specification {

    EventProposalRepository eventProposalRepository = Mock()

    EventProposalService testedObj

    def "Should add new event with partner company"() {
        given: "Prepare tested object and test data"
            prepareTestedObj()
            AuthUser authUser = createPartnerCompanyUser()
            def request = new AddEventProposalRequest()
            request.title = "title"

        when:
            testedObj.addNewEventProposal(authUser, request)

        then: "new event with partner company should be saved"
            eventProposalRepository.save(_) >> { arguments ->
                final EventProposal eventProposal = arguments[0]
                assert !eventProposal.approved
                assert eventProposal.title == "title"
                assert eventProposal.involvedCompany == authUser.user.company
            }
    }

    @Ignore
    def prepareTestedObj() {
        testedObj = new EventProposalService(eventProposalRepository)
    }

    @Ignore
    def createPartnerCompanyUser() {
        def user = new User()
        def authUser = new AuthUser()
        user.authUser = authUser
        authUser.user = user
        def adminRole = new Role()
        adminRole.setName(RoleName.ROLE_PARTNER_USER)
        authUser.setRoles([adminRole] as Set)
        def company = new Company()
        user.company = company
        company.employees = [user] as Set
        authUser
    }
}
