define([
    "dojo/_base/declare",
    "dojo/i18n",
    "scopt/dialog/Dialog",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "scopt/app",
    "scopt/connector",
    "dojo/text!./templates/TenantCreateDialog.html"
], function(declare, i18n, Dialog, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, app, connector, template) {

    var messages = i18n.getLocalization("scopt", "TenantCreateDialog");

    var ContentWidget = declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        messages: messages,
        /*
         * actions:
         */
        _create: function() {
            if (this.form.validate()) {
                var self = this;
                var dialog = this.getParent();
                connector.call("context.tenant.create", {
                    sync: true,
                    data: {
                        tenant_name: this.tenantNameInput.get("value"),
                        params: {
                            "magua.tenant_color": this.tenantColorInput.get("value"),
                            "demo": this.tenantDemoInput.get("checked")
                        }
                    }
                }).then(function(data) {
                    var tenant = {
                        id: data.tenant_id,
                        name: data.tenant_name,
                        color: self.tenantColorInput.get("value")
                    };
                    app.changeTenant(tenant);
                    dialog.hide();
                });
            }
        },
        _hide: function() {
            this.getParent().hide();
        }
    });

    return declare(Dialog, {
        title: messages.createTenant,
        startup: function() {
            this.inherited(arguments);

            this.contentWidget = new ContentWidget();
            this.set("content", this.contentWidget);
        }
    });
});