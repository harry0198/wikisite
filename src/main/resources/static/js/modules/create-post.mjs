import {isFileFormatImage, isFileSizeSensible} from "./validation.mjs";
import {profileImageUpload} from "./util.mjs";

function enableAutoUpdateFunctionality() {
    let form = document.getElementById('form-carousel');
    if (form != null) {
        for (let el of document.getElementsByClassName('title-input')) {
            el.addEventListener('input', function () {
                for (let title of document.getElementsByClassName('card__title')) {
                    title.textContent = this.value;
                }
            });
        }
        for (let el of document.getElementsByClassName('image-input')) {
            el.addEventListener('input', function () {
                if (!isFileFormatImage(profileImageUpload.value)) {
                    return;
                }
                if (!isFileSizeSensible(profileImageUpload.files[0], 4)) {
                    return;
                }
                for (let image of document.getElementsByClassName('card__image')) {
                    image.setAttribute('src',
                        window.URL.createObjectURL(profileImageUpload.files[0]));
                }
            });
        }
        for (let el of document.getElementsByClassName('description-input')) {
            el.addEventListener('input', function () {
                for (let title of document.getElementsByClassName('post-description-content')) {
                    title.textContent = this.value;
                }
            });
        }
    }
}
export {
    enableAutoUpdateFunctionality
}