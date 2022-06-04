import {enableDragNDropFunctionality, stopEnterKeyFormSubmission} from "./modules/dragndrop.mjs";
import {applyDropdownEventHandlers} from "./modules/dropdown.mjs";
import {applyEditableEventHandlers} from "./modules/editable.mjs";
import {applyModalEventHandlers} from "./modules/modal.mjs";
import {enableNavbarFunctionality} from "./modules/navbar.mjs";
import {applyLikeBtnEventHandlers} from "./modules/post-actions.mjs";
import {initializeUtilities} from "./modules/util.mjs";
import {enableCarouselKeyScroll} from "./modules/carousel-key-scroll.mjs";
import {enableAutoUpdateFunctionality} from "./modules/create-post.mjs";
import {enableFeedbackFunctionality} from "./modules/view-post.mjs";
import {enableImageFallbackFunctionality} from "./modules/image-error.mjs";
import Toast from "./modules/toast.mjs";

document.addEventListener('DOMContentLoaded', () => {
    enableDragNDropFunctionality();
    stopEnterKeyFormSubmission();
    applyDropdownEventHandlers();
    applyEditableEventHandlers();
    applyModalEventHandlers();
    enableNavbarFunctionality();
    applyLikeBtnEventHandlers();
    initializeUtilities();
    enableCarouselKeyScroll();
    enableAutoUpdateFunctionality();
    enableFeedbackFunctionality();
    enableImageFallbackFunctionality();

    let clickers = document.getElementsByClassName("click");
    for (let clicker of clickers) {
    clicker.onclick = async () => {
            // await Toast("Saved successfully", "fa-circle-check");
        await Toast("Saved successfully", "fa-circle-exclamation");
            console.log('poof');
        }
    }
});