import {isFileSizeSensible, isFileFormatImage} from "./validation.mjs";

let profileImagePreview;
let profileImageUpload;
const BASE_URL = "http://localhost:8080";

function initializeUtilities() {
    profileImagePreview = document.getElementById('profileImageUpload');
    profileImageUpload = document.getElementById('imageUpload');

    if (profileImageUpload != null && profileImagePreview != null) {
        profileImageUpload.onchange = () => {
            console.log('yep')
            if (profileImageUpload.files && profileImageUpload.files[0]) {
                if (!isFileFormatImage(profileImageUpload.value)) {
                    return;
                }
                if (!isFileSizeSensible(profileImageUpload.files[0].size)) {
                    alert("File is too large! Max file size is 4MB");
                    return;
                }
                profileImagePreview.setAttribute('src',
                    window.URL.createObjectURL(profileImageUpload.files[0]));
            }
        }
    }


    let cards = document.getElementsByClassName('card__container');
    for (let card of cards) {
        let cardTitles = card.getElementsByClassName('card__title');
        let cardImage = card.getElementsByClassName('card__image');
        for (let cardTitle of cardTitles) {
            let aTag = cardTitle.getElementsByTagName('a');
            for (let aTagElement of aTag) {
                let link = aTagElement.getAttribute("href");
                console.log(link);
                if (link != null) {
                    for (let cardImageElement of cardImage) {
                        cardImageElement.onclick = () => {
                            window.location.href = link;
                        }
                    }
                    break;
                }
            }
        }
    }
}
export {
    initializeUtilities,
    profileImageUpload,
    profileImagePreview,
    BASE_URL
}