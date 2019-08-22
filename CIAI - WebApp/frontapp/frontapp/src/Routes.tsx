import * as React from 'react';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Login from "security/components/Login";
import HomepagePanel from "./components/homepage/HomepagePanel";
import PrivateRoute from "./security/components/PrivateRoute";
import EventProposalView from "components/eventProposal/EventProposalView";
import UsersView from "components/users/Users";
import Header from "./common/Header";
import UserForm from "./components/users/UserForm";
import CompanyView from "./components/companies/Companies";
import CompanyForm from "./components/companies/CompanyForm";
import ProfileView from "./components/profile/Profile";
import ReviewsView from "./components/reviews/ReviewsPanel";

const Routes = () =>
    <BrowserRouter>
        <div>
            <Header/>
            <Switch>
                <Route path="/login" component={Login}/>
                <PrivateRoute exact path="/" component={HomepagePanel}/>
                <PrivateRoute path="/eproposal" component={EventProposalView}/>
                <PrivateRoute path="/assignedReviews" component={ReviewsView}/>
                <PrivateRoute path="/users/:userId?" component={UsersView}/>
                <PrivateRoute path="/user/new" component={UserForm}/>
                <PrivateRoute path="/user/edit/:userId" component={UserForm}/>
                <PrivateRoute path="/companies" component={CompanyView}/>
                <PrivateRoute path="/company/new" component={CompanyForm}/>
                <PrivateRoute path="/company/edit/:companyId" component={CompanyForm}/>
                <PrivateRoute path="/profile" component={ProfileView}/>
            </Switch>
        </div>
    </BrowserRouter>;


export default Routes;