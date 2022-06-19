import {enableDragNDropFunctionality, stopEnterKeyFormSubmission} from "./modules/dragndrop.mjs";
import {applyDropdownEventHandlers} from "./modules/dropdown.mjs";
import {enableNavbarFunctionality} from "./modules/navbar.mjs";
import {applyLikeBtnEventHandlers} from "./modules/post-actions.mjs";
import {initializeUtilities} from "./modules/util.mjs";
import {enableCarouselKeyScroll} from "./modules/carousel-key-scroll.mjs";
import {enableImageFallbackFunctionality} from "./modules/image-error.mjs";
import Dialog from "./modules/dialog.mjs";
import {enableSidebarFunctionality} from "./modules/sidebar.mjs";

document.addEventListener('DOMContentLoaded', () => {
    enableDragNDropFunctionality();
    stopEnterKeyFormSubmission();
    applyDropdownEventHandlers();
    enableNavbarFunctionality();
    applyLikeBtnEventHandlers();
    initializeUtilities();
    enableCarouselKeyScroll();
    enableFeedbackFunctionality();
    enableImageFallbackFunctionality();
    document.querySelectorAll('dialog[modal-mode="mega"]').forEach(dialog => {
        Dialog(dialog);
    })

    enableSidebarFunctionality();
});