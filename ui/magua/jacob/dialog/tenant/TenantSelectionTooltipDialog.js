/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

define([
    "dojo/_base/declare",
    "dojo/i18n",
    "dijit/popup",
    "dijit/TooltipDialog",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "jacob/dialog/tenant/TenantCreateDialog",
    "jacob/store/DataStore",
    "jacob/connector",
    "jacob/app",
    "dojo/text!./templates/TenantSelectionTooltipDialog.html"
], function(declare, i18n, popup, TooltipDialog, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, TenantCreateDialog, DataStore, connector, app, template) {

    var messages = i18n.getLocalization("jacob", "TenantSelectionTooltipDialog");

    var ContentWidget = declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        tenantStore: null,
        messages: messages,
        /*
         * actions:
         */
        realoadTenants: function() {
            this.tenantStore = new DataStore({
                idProperty: "tenantId",
                itemsProperty: "tenants",
                message: "context.tenant.findByLogin",
                requestData: {
                    login: app.getPrincipal().login
                }
            });
            this.tenantInput.setStore(this.tenantStore);

            var tenant = app.getTenant();
            if (tenant)
                this.tenantInput.set("value", tenant.id);

            if (!this.tenantInput.getOptions().length) {
                this.tenantInput.set("disabled", true);
                this.selectButton.set("disabled", true);
            } else {
                this.tenantInput.set("disabled", false);
                this.selectButton.set("disabled", false);
            }
        },
        _select: function() {
            var dialog = this.getParent();
            var tennantId = this.tenantInput.get("value");
            var tenant = {
                id: tennantId,
                name: this.tenantStore.get(tennantId).tenantName
            };
            connector.call("context.tenant.paramGet", {
                data: {
                    tenantId: tennantId,
                    paramNames: ["magua.tenantColor"]
                }
            }).then(function(data) {
                tenant.color = data.paramValues["magua.tenantColor"];
                app.changeTenant(tenant);
                popup.close(dialog);
            });
        },
        _createNew: function() {
            var dialog = new TenantCreateDialog();
            dialog.show();
            popup.close(this.getParent());
        }
    });

    return declare(TooltipDialog, {
        style: "width: 380px;",
        startup: function() {
            this.inherited(arguments);

            this.contentWidget = new ContentWidget();
            this.set("content", this.contentWidget);
        },
        onShow: function() {
            this.contentWidget.realoadTenants();
        }
    });
});