/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var dojoConfig = {
    baseUrl: ".",
    locale: "en-EN",
    extraLocale: ["sk-SK"],
    tlmSiblingOfDojo: false,
    packages: [
        {name: "dojo", location: "dojo"},
        {name: "dijit", location: "dijit"},
        {name: "dojox", location: "dojox"},
        {name: "jacob", location: "jacob", main: "app"},
        {name: "jacobsystem", location: "jacobsystem"}
    ]
};

var contentViews = [
    {
        id: "greetingView",
        hidden: true,
        view: "jacob/view/main/GreetingView"
    }, {
        id: "dashboardView",
        code: "00",
        view: "jacob/view/main/DashboardView"
    },
    {
        id: "system",
        views: [
            {
                id: "settings",
                views: [
                    {
                        id: "unitOfMeasuresView",
                        code: "01",
                        view: "jacobsystem/view/settings/UnitOfMeasuresView"
                    },
                    {
                        id: "addressTypesView",
                        code: "02",
                        view: "jacobsystem/view/settings/AddressTypesView"
                    },
                    {
                        id: "mediaTypesView",
                        code: "03",
                        view: "jacobsystem/view/settings/MediaTypesView"
                    },
                    {
                        id: "partnersView",
                        code: "04",
                        view: "jacobsystem/view/settings/PartnersView"
                    }
                ]
            },
            {
                id: "developerTools",
                views: [
                    {
                        id: "registryView",
                        code: "9999",
                        view: "jacobsystem/view/registry/RegistryView"
                    }
                ]
            }
        ]
    }
];

var contentConnections = [
    {
        sourceView: "searchItem",
        sourceEvent: "onShowItemDetail",
        targetView: "updateItem",
        targetEvent: "onShowItemDetail"
    }, {
        sourceView: "createItem",
        sourceEvent: "onShowItemDetail",
        targetView: "updateItem",
        targetEvent: "onShowItemDetail"
    }
];