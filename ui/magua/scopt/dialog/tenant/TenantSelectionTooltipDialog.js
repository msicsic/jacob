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
    "scopt/dialog/tenant/TenantCreateDialog",
    "scopt/store/DataStore",
    "scopt/connector",
    "scopt/app",
    "dojo/text!./templates/TenantSelectionTooltipDialog.html"
], function(declare, i18n, popup, TooltipDialog, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, TenantCreateDialog, DataStore, connector, app, template) {

    var messages = i18n.getLocalization("scopt", "TenantSelectionTooltipDialog");

    var ContentWidget = declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        tenantStore: null,
        messages: messages,
        /*
         * actions:
         */
        realoadTenants: function() {
            this.tenantStore = new DataStore({
                idProperty: "tenant_id",
                itemsProperty: "tenants",
                message: "context.tenant.find_by_login",
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
                name: this.tenantStore.get(tennantId).tenant_name
            };
            connector.call("context.tenant.param_get", {
                data: {
                    tenant_id: tennantId,
                    param_names: ["magua.tenant_color"]
                }
            }).then(function(data) {
                tenant.color = data["magua.tenant_color"];
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