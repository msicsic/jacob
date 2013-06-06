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
        {name: "scopt", location: "scopt", main: "app"},
        {name: "scoptsystem", location: "scoptsystem"},
        {name: "scoptinventory", location: "scoptinventory"}
    ]
};

var contentViews = [
    {
        id: "greetingView",
        hidden: true,
        view: "scopt/view/main/GreetingView"
    }, {
        id: "dashboardView",
        code: "00",
        view: "scopt/view/main/DashboardView"
    },
//    {
//        id: "inventory",
//        views: [
//            {
//                id: "searchItemView",
//                code: "0100",
//                view: "scoptinventory/view/item/SearchItemView"
//            }, {
//                id: "createItemView",
//                code: "0110",
//                view: "scoptinventory/view/item/CreateItemView"
//            }, {
//                id: "updateItemView",
//                hidden: true,
//                code: "0111",
//                view: "scoptinventory/view/item/UpdateItemView"
//            }
//        ]
//    },
    {
        id: "system",
        views: [
            {
                id: "settings",
                views: [
                    {
                        id: "unitOfMeasuresView",
                        code: "01",
                        view: "scoptsystem/view/settings/UnitOfMeasuresView"
                    },
                    {
                        id: "addressTypesView",
                        code: "02",
                        view: "scoptsystem/view/settings/AddressTypesView"
                    },
                    {
                        id: "mediaTypesView",
                        code: "03",
                        view: "scoptsystem/view/settings/MediaTypesView"
                    },
                    {
                        id: "partnersView",
                        code: "04",
                        view: "scoptsystem/view/settings/PartnersView"
                    }
                ]
            },
            {
                id: "developerTools",
                views: [
                    {
                        id: "registryView",
                        code: "9999",
                        view: "scoptsystem/view/registry/RegistryView"
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