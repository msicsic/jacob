define(function() {

    return {
        /*
         * Local
         */
        localGet: function(key) {
            var value = window.localStorage.getItem(key);
            return eval("(" + value + ")");
        },
        localPut: function(key, value) {
            window.localStorage.setItem(key, JSON.stringify(value));
        },
        localRemove: function(key) {
            window.localStorage.removeItem(key);
        },
        localClear: function() {
            window.localStorage.clear();
        },
        /*
         * Session
         */
        sessionGet: function(key) {
            var value = window.sessionStorage.getItem(key);
            return eval("(" + value + ")");
        },
        sessionPut: function(key, value) {
            window.sessionStorage.setItem(key, JSON.stringify(value));
        },
        sessionRemove: function(key) {
            window.sessionStorage.removeItem(key);
        },
        sessionClear: function() {
            window.sessionStorage.clear();
        }
    };
});