define([
    "dojo/_base/kernel",
    "dojo/topic",
    "dojo/ready",
    "dojo/hash",
    "jacob/storage",
    "jacob/connector",
    "dojo/Deferred"
], function(kernel, topic, ready, hash, storage, connector, Deferred) {
    var principal = storage.sessionGet("app.principal");
    var tenant = storage.sessionGet("app.tenant");

    topic.subscribe("/connector/logout", function() {
        tenant = null;
        principal = null;
        hierarchyContainer.clearAllViews();
        hierarchyContainer.selectView("greetingView");
        menuNavigation.set("disabled", true);
        topic.publish("/app/logout");
        storage.sessionClear();
        window.location.reload();
    });

    ready(function() {
        hierarchyContainer.validateShowView = function(viewId) {
            var retViewId = viewId;
            if (!tenant && viewId !== "greetingView") {
                retViewId = "greetingView";
            }
            return retViewId;
        };
        var currentHash = hash();
        hierarchyContainer.publishParameters = function(viewId, parameters) {
            currentHash = hash(viewId + "/" + parameters.join("/"));
        };

        topic.subscribe("hierarchyContainer/selectView", function(view) {
            currentHash = hash(view.id);
        });
        topic.subscribe("hierarchyContainer/viewTitleChanged", function(title) {
            app.setViewTitle(title);
        });
        topic.subscribe("/dojo/hashchange", function(hash) {
            if (currentHash !== hash) {
                var viewId = hash.split("/")[0];
                var params = hash.split("/").slice(1);
                if (viewId !== hierarchyContainer.selectedView.id) {
                    hierarchyContainer.selectView(viewId, params);
                } else {
                    hierarchyContainer.selectedView.onSelect(params);
                }
            }
        });

        var viewId = hash().split("/")[0];
        var params = hash().split("/").slice(1);
        if (viewId !== "") {
            hierarchyContainer.selectView(viewId, params);
        } else if (tenant) {
            hierarchyContainer.selectView("dashboardView");
        } else {
            hierarchyContainer.selectView("greetingView");
        }

        menuNavigation.set("disabled", !tenant);
    });

    var app = {
        changeTenant: function(newTenant) {
            if (!tenant || tenant.id !== newTenant.id) {
                tenant = newTenant;
                storage.sessionPut("app.tenant", tenant);
                connector.setHeaderParameter("tenantId", tenant.id);

                topic.publish("/app/tenantChanged");

                hierarchyContainer.clearAllViews();
                hierarchyContainer.selectView("dashboardView");
                menuNavigation.set("disabled", false);
            }
        },
        getTenant: function() {
            return tenant;
        },
        login: function(userName, userPassword) {
            var deferred = new Deferred();
            connector.login(userName, userPassword).then(function(newPrincipal) {
                principal = newPrincipal;
                storage.sessionPut("app.principal", principal);

                topic.publish("/app/login");
                deferred.resolve();
            }, function(message) {
                deferred.reject(message);
            });
            return deferred.promise;
        },
        getPrincipal: function() {
            return principal;
        },
        changeLocale: function(value) {
            if (value !== kernel.locale) {
                storage.localPut("app.locale", value);
                window.location.reload();
            }
            return false;
        },
        logout: function() {
            connector.logout();
        },
        setViewTitle: function(title) {
            if (title) {
                topic.publish("/app/titleChanged", title);
                document.title = "JACOB - " + title;
            } else {
                topic.publish("/app/titleChanged", "JACOB");
                document.title = "JACOB";
            }
        }
    };
    return app;
});