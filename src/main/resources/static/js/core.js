

// Navbar logic
let menu = document.querySelector('#menu-bars');
let navbar = document.querySelector('.navbar');
let search = document.querySelector('#search');

if (menu != null){
    menu.onclick = () => {
        menu.classList.toggle('fa-times');
        navbar.classList.toggle('active');
    }
}

if (search != null) {
    search.onclick = () => {
        window.location.href = "https://www.vesudatutorials.com/search";
    }
}

let openMenu = document.querySelectorAll('.nav-menu-btn');
let m = document.querySelector('.site-nav');

openMenu.forEach(btn => {
    btn.onclick = () => {
        m.classList.toggle('active');
    }
});

// modal logic



const openModalButtons = document.querySelectorAll("[data-modal-target]");
const closeModalButtons = document.querySelectorAll("[data-close-button]");
const overlay = document.getElementById('overlay');

if (overlay != null && openModalButtons != null && closeModalButtons != null) {
    openModalButtons.forEach(button => {
        button.addEventListener('click', () => {
            const modal = document.querySelector(button.dataset.modalTarget)
            openModal(modal);
        });
    })
    closeModalButtons.forEach(button => {
        button.addEventListener('click', () => {
            const modal = button.closest('._modal');
            closeModal(modal);
        });
    });

    overlay.addEventListener('click', () => {
        const modals = document.querySelectorAll('._modal.active');
        modals.forEach(modal => {
            closeModal(modal);
        })
    })

    function openModal(modal) {
        if (modal == null) {
            return;
        }
        modal.classList.add('active');
        overlay.classList.add('active');
    }

    function closeModal(modal) {
        if (modal == null) {
            return;
        }
        modal.classList.remove('active');
        overlay.classList.remove('active');
    }
}

let profileImagePreview = document.getElementById('profileImageUpload');
let profileImageUpload = document.getElementById('imageUpload');

if (profileImageUpload != null && profileImagePreview != null) {
    profileImageUpload.onchange = () => {
        if (profileImageUpload.files && profileImageUpload.files[0]) {
            profileImagePreview.setAttribute('src',
                window.URL.createObjectURL(profileImageUpload.files[0]));
        }
    }
}

//
// for (let card of cards) {
//     let cardId = card.getAttribute('data-el_id');
//     let cardSaveBtn = card.getElementsByClassName('card__save_btn');
//     let cardLikeBtn = card.getElementsByClassName('card__like_btn');
//     for (let cardSaveBtnElement of cardSaveBtn) {
//         cardSaveBtnElement.onclick = () => {
//             let saved = cardSaveBtnElement.getAttribute('data-el_saved');
//             if (saved == "true") {
//                 updateSave(cardId, false);
//                 cardSaveBtnElement.setAttribute('data-el_saved', 'false');
//             } else {
//                 updateSave(cardId, true);
//                 cardSaveBtnElement.setAttribute('data-el_saved', 'true');
//             }
//         }
//     }
//     for (let cardLikeBtnElement of cardLikeBtn) {
//         cardLikeBtnElement.onclick = () => {
//             let liked = cardLikeBtnElement.getAttribute('data-el_liked');
//             if (liked === "true") {
//                 updateLikes(cardId, false);
//                 cardLikeBtnElement.setAttribute('data-el_liked', 'false');
//             } else {
//                 updateLikes(cardId, true);
//                 cardLikeBtnElement.setAttribute('data-el_liked', 'true');
//             }
//         }
//     }
//
// }

function updateSave(id, save) {
    const url = "http://localhost:8080/api/v1/post/save";
    fetch(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',

        },
        body: JSON.stringify({"postId":id, "save":save})
    })
        .then(res => {
            if (!res.ok) return; // error

            let selector = "[data-el_id=\"" + id + "\"]";
            let card = document.querySelectorAll(selector);
            for (let cardElement of card) {
                for (let cardSaveBtn of cardElement.getElementsByClassName('card__save_btn')) {
                    if (save) {
                        cardSaveBtn.classList.add('selected');
                    } else {
                        cardSaveBtn.classList.remove('selected');
                    }
                }
            }
        });

}

function updateLikes(id, like) {
    const url = "http://localhost:8080/api/v1/post/like";
    fetch(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"postId":id, "like":like})
    })
        .then(res => {
            if (!res.ok) return; // error

            let selector = "[data-el_id=\"" + id + "\"]";
            let card = document.querySelectorAll(selector);
            for (let cardElement of card) {
                for (let cardSaveBtn of cardElement.getElementsByClassName('card__like_btn')) {
                    if (like) {
                        cardSaveBtn.classList.add('selected');
                    } else {
                        cardSaveBtn.classList.remove('selected');
                    }
                }
            }
        });

}

// fetch("http://localhost:8080/api/v1/post/new", {
//     method: "POST",
//     headers: {
//         'Content-Type': 'application/json',
//     },
//     body: JSON.stringify({"title":"test", "description":"test", "imageRequests":[]})
// })
//     .then(res => {
//
//     });
//
let contentEditor = document.getElementById("content-editor");
if (contentEditor != null) {
    let simplemde = new SimpleMDE({ element: contentEditor });
}

let dropdownMenus = document.getElementsByClassName('dropdown__menu');
for (let dropdownMenu of dropdownMenus) {
    for (let dropdownBtn of dropdownMenu.getElementsByClassName('dropdown__btn')) {

        dropdownBtn.onclick = () => {
            dropdownMenu.classList.toggle('open');
        }
    }

    document.addEventListener("click", (event) => {
        const isClickInside = dropdownMenu.contains(event.target);

        if (dropdownMenu.classList.contains('open') && !isClickInside) {
            dropdownMenu.classList.remove('open');
        }
    });
}

let cards = document.getElementsByClassName('card__container');
for (let card of cards) {
    let cardTitles = card.getElementsByClassName('card__title');
    for (let cardTitle of cardTitles) {
        let aTag = cardTitle.getElementsByTagName('a');
        for (let aTagElement of aTag) {
            let link = aTagElement.getAttribute("href");
            if (link != null) {
                card.onclick = () => {
                    window.location.href = link;
                }
                 break;
            }
        }
    }
}