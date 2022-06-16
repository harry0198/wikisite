import {init as commons, onLoad} from "./common.mjs";
import {setLightMode, setDarkMode, clearColorPreference} from "../theme-toggle.js";
import {isLink, validationPassed, validationFailed} from "../modules/form-validation.js";
import {toJsonObject, submitFormRequest} from "../modules/ajax.mjs";
import Toast from "../modules/toast.mjs";
import {enableDragNDropFunctionality} from "../modules/dragndrop.mjs";
import {enableSidebarFunctionality} from "../modules/sidebar.mjs";
enableSidebarFunctionality()

let id;

onLoad(() => {
    commons()
    addEventHandlers()
    enableDragNDropFunctionality()
    enableSidebarFunctionality()
    id = document.querySelector('meta[name="id"]').content;
});

function addEventHandlers() {
    let saveBtn = document.getElementById('save-btn');
    if (saveBtn != null) {
        saveBtn.onclick = (e) => {
            e.preventDefault();
            saveBtnHandler(saveBtn);
        }
    }
    let systemTheme = document.getElementById('theme-system');
    if (systemTheme != null) {
        systemTheme.onclick = () => {
            clearColorPreference();
            Toast("Theme updated", "fa-circle-check");
        }
    }
    let lightTheme = document.getElementById('theme-light');
    if (lightTheme != null) {
        lightTheme.onclick = () => {
            setLightMode()
            Toast("Theme updated", "fa-circle-check");
        }
    }
    let darkTheme = document.getElementById('theme-dark');
    if (darkTheme != null) {
        darkTheme.onclick = () => {
            setDarkMode();
            Toast("Theme updated", "fa-circle-check");
        }
    }

    let updatePreferences = document.getElementById('update-preferences');
    if (updatePreferences != null) {
        updatePreferences.onclick = (e) => {
            e.preventDefault();
            updatePreferencesHandler(updatePreferences);
        }
    }


}
function updatePreferencesHandler(btn) {

    btn.classList.add('button--loading');

    let form = document.getElementById("edit_preferences-form"),
        actionPath = "";

    let formData = new FormData(form);
    actionPath = "/api/user/"+id+"/preferences";

    let cb = (e) => {
        console.log('called');
        let status = e.status;
        if (status === 400) {
            Toast("Failed to save changes", "fa-circle-exclamation");
        } else if (status === 200) {
            Toast("Preferences Updated", "fa-circle-check");
        } else {
            Toast("Unexpected error! Please try again later.", "fa-circle-exclamation")
        }
        btn.classList.remove('button--loading');
    }

    submitFormRequest(actionPath, cb, formData, 'POST');
}

function saveBtnHandler(btn) {

    if (!validateUsernameField() || !validateBioField() || !validateLinkField()) {
        Toast("Failed to save changes", "fa-circle-exclamation");
        return;
    }

    btn.classList.add('button--loading');

    let form = document.getElementById("edit_profile-form"),
        actionPath = "";

    let formData = new FormData(form);
    actionPath = "/api/user/"+id;

    let cb = (e) => {
        let status = e.status;
        if (status === 400) {
            Toast("Failed to save changes", "fa-circle-exclamation");

            let obj = toJsonObject(e.responseText);

            if (obj.username != null) {
                let usernameField = document.getElementsByName('username');
                usernameField.forEach(field =>  validationFailed(field, obj.username))
            }
        } else if (status === 200) {
            Toast("Changes saved successfully", "fa-circle-check");
        } else {
            Toast("Unexpected error! Please try again later.", "fa-circle-exclamation")
        }
        btn.classList.remove('button--loading');
    }

    submitFormRequest(actionPath, cb, formData, 'POST');
}

function validateBioField() {
    let bioField = document.getElementById("user_bio");
    if (bioField != null) {
        if (bioField.value === '') {
            validationPassed(bioField);
            return true;
        } else if (bioField.value.length > 1500) {
            validationFailed(bioField, "Bio should be short! (< 1500 characters)");
            return false;
        } else {
            validationPassed(bioField);
            return true;
        }
    }
}

function validateLinkField() {
    let linkField = document.getElementById('user_link');
    if (linkField != null) {
        if (linkField.value === '') {
            validationPassed(linkField);
            return true;
        }
        if (!isLink(linkField.value)) {
            validationFailed(linkField, "Oops! This link is invalid!");
            return false;
        } else if (linkField.value.length > 100) {
            validationFailed(linkField, "Make links easy to read! < 200 characters please!");
            return false;
        } else {
            validationPassed(linkField);
            return true;
        }
    }
}

function validateUsernameField() {
    let usernameField = document.getElementById('user_username');
    if (usernameField != null) {
        let username = usernameField.value;
        if (username.length <= 3) {
            validationFailed(usernameField, "Usernames should be more than 3 characters!")
            return false;
        } else if(username.length > 20) {
            validationFailed(usernameField, "Usernames should be less than 20 characters!")
            return false;
        } else {
            validationPassed(usernameField);
            return true;
        }
    }
}