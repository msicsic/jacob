/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

define([
    "dojo/_base/declare",
    "dojo/i18n",
    "dojo/string",
    "dojo/dom-style",
    "scopt/storage",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "scopt/app",
    "dojo/text!./templates/HeaderWidget.html"
], function(declare, i18n, string, domStyle, storage, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, app, template) {
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
        postMixInProperties: function() {
            this.inherited(arguments);
            this.messages = i18n.getLocalization("scopt", "HeaderWidget");
        },
        postCreate: function() {
            this.inherited(arguments);

            this.titleNode.innerHTML = app.title;

            this.subscribe("/app/logout", this._onLogout);
            this.subscribe("/app/login", this._onLogin);
            this.subscribe("/app/tenantChanged", this._onChangeTenant);
            this.subscribe("/app/titleChanged", this._onChangeTitle);
        },
        startup: function() {
            this.inherited(arguments);

            var principal = app.getPrincipal();
            if (principal) {
                this.loggedInNode.innerHTML = string.substitute(this.messages.loggedAs, principal);
                domStyle.set(this.loginDropDownButton.domNode, "display", "none");

                var tenant = app.getTenant();
                if (tenant) {
                    this.tenantDropDownButton.set("label", string.substitute(this.messages.buttonSelectedTenant, tenant));
                    domStyle.set(this.domNode, "borderBottomColor", tenant.color || "transparent");
                }

                if (true === storage.sessionGet("headerView.showingTenantTooltipDialog")) {
                    this.tenantDropDownButton.openDropDown();
                }
            } else {
                if (true === storage.sessionGet("headerView.showingLoginTooltipDialog")) {
                    this.loginDropDownButton.openDropDown();
                }

                this.tenantDropDownButton.set("disabled", true);
                domStyle.set(this.logoutButton.domNode, "display", "none");
            }
        },
        /*
         * actions:
         */
        _onLogout: function() {
            domStyle.set(this.loginDropDownButton.domNode, "display", "");
            domStyle.set(this.logoutButton.domNode, "display", "none");
            this.loggedInNode.innerHTML = "";
            this.tenantDropDownButton.set("disabled", true);
            this.tenantDropDownButton.set("label", this.messages.buttonSelectTenant);
            domStyle.set(this.domNode, "borderBottomColor", "transparent");
        },
        _onLogin: function() {
            domStyle.set(this.loginDropDownButton.domNode, "display", "none");
            domStyle.set(this.logoutButton.domNode, "display", "");
            this.loggedInNode.innerHTML = string.substitute(this.messages.loggedAs, app.getPrincipal());

            this.tenantDropDownButton.set("disabled", false);
            this.tenantDropDownButton.openDropDown();
            this.tenantDropDownButton.dropDown.focus();
        },
        _onChangeTenant: function() {
            var tenant = app.getTenant();
            this.tenantDropDownButton.set("label", string.substitute(this.messages.buttonSelectedTenant, tenant));
            domStyle.set(this.domNode, "borderBottomColor", tenant.color || "transparent");
        },
        _onChangeTitle: function(title) {
            this.titleNode.innerHTML = title;
        },
        _logout: function() {
            app.logout();
        },
        _onShowTenantTooltipDialog: function() {
            storage.sessionPut("headerView.showingTenantTooltipDialog", true);
        },
        _onCloseTenantTooltipDialog: function() {
            storage.sessionRemove("headerView.showingTenantTooltipDialog");
        },
        _onShowLoginTooltipDialog: function() {
            storage.sessionPut("headerView.showingLoginTooltipDialog", true);
        },
        _onCloseLoginTooltipDialog: function() {
            storage.sessionRemove("headerView.showingLoginTooltipDialog");
        }
    });
});