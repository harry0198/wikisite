
import {initializeUtilities} from "../modules/util.mjs";
import {enableNavbarFunctionality} from "../modules/navbar.mjs";
import {enableImageFallbackFunctionality} from "../modules/image-error.mjs";
import Dialog from "../modules/dialog.mjs"

function init() {

    initializeUtilities()
    enableNavbarFunctionality()
    enableImageFallbackFunctionality()
    document.querySelectorAll('dialog[modal-mode="mega"]').forEach(dialog => {
        Dialog(dialog);
    })
}

function onLoad(func) {
    if (document.readyState === 'complete') {
        func()
    } else {
        document.addEventListener('DOMContentLoaded', func);
    }
}

export {
    init, onLoad
}