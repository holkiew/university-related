# User stories
----

#### TODO in BACKEND:
- Add endpoint for getting ECMA_USERs assigned reviews

### Compoments tree:
* Header
* Login
     * login form 
* Home/Events
     * List of all Events
     * Event detail with discussion
* My profile
     * Logged user detail
     * Edit form
     * Delete account action
     * Register new user to company by partner admin 
* Company (Partner company detail)
     * Company detail
     * Company edit form
     * Company users list 
* AssignedReviews
     * List of EventProposals to review 
* Users
   * List of all users
   * User detail
   * Delete user action
   * User edit form
   * Register new user by ecma admin
* Companies
   * List of all companies
   * Company detail
   * Company users list
   * Delete company action
   * Company edit form/add form
* EventProposals
   * List of all eventProposals
   * EventProposal create/edit form
   * EventProposal detail
       * Delete Event proposal action
       * Approve Event proposal action
       * Assign current user to review action
       * Assign any user by ECMA_ADMIN action
       * Create review form
       * Review detail
       * Discussion
           *  Create new comment
           *  Delete comment by admin

## Stories 
---
##### IMPORTANT NOTES: 
**ECMA_USER** = any user from ECMA, that means also **ECMA_ADMIN**
**PARTNER_USER** = any user from partner company, that means also **PARTNER_ADMIN**
**logged user** = ECMA_USER or PARTNER_USER

### Header:
As **Logged user**, I select **Home page** from app header to go to **Home page**.
As **Logged user**, I select **Event Proposals** from app header to go to **EventProposal page**.
As **Logged user**, I select **My Profile** from app header to go to **My Profile page**.
As **Logged user**, I select **Logout** from app header to **logout** from app and see** login page**.
As **PARTNER_USER**, I select **Company** from app header to go to **Company page**.
As **ECMA_USER**, I select **Assigned reviews** from app header to go to **Assigned reviews page**.
As **ECMA_ADMIN**, I select **Companies** from app header to go to **Companies page**.
As **ECMA_ADMIN**, I select **Users** from app header to go to **Users page**.


### Login:
As **not logged user**, I open its Login page, **sign in** and see **home page**.

### Home/Event:
As **PARTNER_USER**, I open its home page, select an Event from **list of most recent events (approved EventProposals)** to see **event detail**, where can also see **comment stream** for this event.

### My Profile:
As **logged user**, I open its My profile page so that I can see my **user detail**.
As **logged user**, I open its My profile page, select **"Edit" action** to go to **edit user form**.
As **logged user**, I open its **edit user form**, change my name, phone, email or password to see my edited info on My Profile page.
As **logged user**, I open its My Profile page, select **"delete account" action** to **delete my account**.

### Company:
As **PARTNER_ADMIN**, I open its Company page to see my **company detail**.
As **PARTNER_ADMIN**, I open its Company page, select **"Edit" action** to open **edit company form**.
As **PARTNER_ADMIN**, I open its **Edit company page**, change **company name or address** and see the changed detail on **company detail**.
As **PARTNER_ADMIN**, I open its Company page, **select user** from **company users list** to see his **user detail**.
As **PARTNER_ADMIN**, I open its Company page, select **"add new user" action** to open **add user form**, where I can create only partner user or partner admin, to **create new partner user or partner admin for my company** and see him in User list on My Company page.

### AssignedReviews:
As **ECMA_USER**, I open Assigned reviews page, select **Event proposal** from **list of assigned eventProposal to review** to go to **EventProposal detail page**.

### Users:
As **ECMA_ADMIN**, I open its users page, select **user** from **list of all users** to see **user detail**.
As **ECMA_ADMIN**, I open its **user detail**, select **"Delete" action** to **delete user account**.
As **ECMA_ADMIN**, I open its **user detail**, select **"Edit" action** to open **user edit form** to edit user detail.
As **ECMA_ADMIN**, I open its **user edit form**, change any user detail to see changed user in Users list on Users page.
As **ECMA_ADMIN**, I open its users page, select **"create new user" action** to see **add user form**, where I can create user with any role available, to **create new user with any role** and see him in User list on Users page.

### Companies:
As **ECMA_USER**, I open its Companies page, select **company** from **list all companies** to see **company detail and company users list**.
As **ECMA_ADMIN**, I open its Companies page, select **company** from **list all companies**, select user from **company users list** to see **user detail** on Users page.
As **ECMA_ADMIN**, I open its Companies page, select **company** from **list all companies**, select **edit action** to edit company detail in **company edit form**.
As **ECMA_ADMIN**, I open its Companies page, select **company** from **list all companies**, select **delete action** to delete company with all company users.
As **ECMA_ADMIN**, I open its Companies page, select **"create new" action** to open **add company form**.
As **ECMA_ADMIN**, I complete its **add company form** to see new company in companies list on companies page.

### Event Proposals:
As **logged user**, I open its Event Proposals page, select **Event proposal** from **event proposals list (that are not approved yet)**, that I can see **Event proposal detail**, where I also see **comment stream** for this event proposal.
As **logged user**, I open its  **Event proposal detail** to see also Event proposal reviewer if it is already available.
As **logged user**, I open its Event Proposals page, select **"Create new" action** to see **Add Event proposal form**.
As **logged user**, I open its **Add Event proposal form**, complete it and see it in event proposal list on Event Proposal page.
As **user with Proposal approvement right**, I open its **Event proposal detail** of event proposal which has review, select **"Approve"** to **approve event proposal**, see it on Home page in **Events list**.
As **ECMA_ADMIN**, I open its **Event proposal detail**, select **"Delete" action** to **delete event proposal** and see **updated proposal list** on Event Proposal page.
As **ECMA_USER**, I open its **Event proposal detail** of event proposal without assigned reviewer, select **"Assign me" action*** to assign myself as reviewer.
As **ECMA_ADMIN**, I open its **Event proposal detail** of event proposal without reviewer, select **"assign user"**, select user from **list of users for reviewing** to **assign reviewer for event proposal**.
As **ECMA_USER**, I open **Event proposal detail** of event proposal, that I am assigned to review to open **Add review form**.
As **ECMA_USER**, I open **Add review form**, complete it to see Review on **Event proposal detail**.
As **logged user**, I open its **Event proposal detail** of event proposal I am involved in to see **event proposal discussion**.
As **logged user**, I open its **Event proposal detail** of event proposal I am involved in, select **"Add comment" action** in **discussion**, write comment to see my new comment in discussion.
As **ECMA_ADMIN**, II open its **Event proposal detail**, select **"delete comment" action** on comment in **discussion** to **delete comment**.


