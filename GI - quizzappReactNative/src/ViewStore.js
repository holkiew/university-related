class ViewStore {
    displayView = ["FirstView", "SecondView", "ThirdView", "FourthView", "FifthView", "TestView"];
    secondTry = false;

    getNextView(currentView) {
        let currentViewIndex = this.displayView.findIndex((s) => s.includes(currentView))
        console.info("CurrentView: ", currentView, " NextView: ", this.displayView[currentViewIndex + 1])
        return this.displayView[currentViewIndex + 1]
    }

    setView(oldView, newView) {
        let oldViewIndex = this.displayView.findIndex((s) => s === oldView)
        this.displayView[oldViewIndex] = newView
    }
}
const viewStore = new ViewStore();
export default viewStore