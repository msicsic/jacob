define([
    "dojo/_base/declare",
    "dojo/store/api/Store",
    "dojo/_base/lang",
    "dojo/store/util/QueryResults",
    "scopt/connector"
], function(declare, Store, lang, QueryResults, connector) {
    return declare([Store], {
        idProperty: "id",
        itemsProperty: "items",
        totalProperty: "total",
        message: null,
        constructor: function(options) {
            lang.mixin(this, options);
        },
        query: function(query, options) {            
            var requestQuery = {};
            if (query) {
                switch (typeof query) {
                    case "object":
                        for (var key in query) {
                            var entry = query[key];
                            requestQuery[key] = entry.toString();
                        }
                        break;
                    case "string":
                        requestQuery = query;
                        break;
                    case "function":
                        throw new Error("Funcion query not implented");
                        break;
                    default:
                        throw new Error("Can not query with a " + typeof query);
                }
            }

            options = options || {};
            var reqeustSort = [];
            for (var i in options.sort) {
                var entry = options.sort[i];
                reqeustSort[i] = (entry.descending ? "-" : "+") + entry.attribute;
            }
            
            var requestData = {
                query: requestQuery,
                start: options.start || 0,
                count: options.count || Infinity,
                sort: reqeustSort
            };

            var ret = null;
            var self = this;
            connector.call(this.message, {
                sync: true,
                data: requestData
            }).then(function(data) {
                var items = data[self.itemsProperty];
                items.total = data[self.totalProperty];
                ret = QueryResults(items);
            });
            return ret;
        },
        get: function(id) {
            var requestData = {
                start: 0,
                count: 1,
                sort: []
            };
            requestData.query = {};
            requestData.query[this.idProperty] = id;

            var ret = null;
            var self = this;
            connector.call(this.message, {
                sync: true,
                data: requestData
            }).then(function(data) {
                var items = data[self.itemsProperty];
                if (!items || !items.length) {
                    return null;
                } else {
                    ret = items[0];
                }
            });
            return ret;
        }
    });
});

                