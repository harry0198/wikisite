

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
            console.log('oka');
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

let cards = document.getElementsByClassName('article__card');
if (cards != null) {
    for (let card of cards) {
        let cardLink = card.getElementsByClassName('article__card-title');
        let link;
        for (let cardLinkElement of cardLink) {
            link = cardLinkElement.querySelector('a').href;
        }
        let cardHeader = card.getElementsByClassName('article__card-header');
        let cardOverview = card.getElementsByClassName('article__card-overview');
        for (let cardHeaderElement of cardHeader) {
            cardHeaderElement.onclick = () => {
                window.location.href = link;
            }
        }
        for (let cardInfoElement of cardOverview) {
            cardInfoElement.onclick = () => {
                window.location.href = link;
            }
        }
    }
}


let left_mover_btn = document.getElementById('article__popular-move-left');
let right_mover_btn = document.getElementById('article__popular-move-right');
let cardTmp = document.getElementById('article__popular-cards');
let carouselPosts;
if (cardTmp != null) {
    carouselPosts = cardTmp.getElementsByClassName('article__card');
}
if (left_mover_btn != null && right_mover_btn != null && carouselPosts != null) {
    registerCarouselMovers(carouselPosts, left_mover_btn, right_mover_btn);
}

let left_mover_btn_rec = document.getElementById('article__recommended-move-left');
let right_mover_btn_rec = document.getElementById('article__recommended-move-right');
let pstTmp = document.getElementById('article__recommended-cards')
let recommendedPosts;
if (pstTmp != null) {
    recommendedPosts = pstTmp.getElementsByClassName('article__card');
}
if (left_mover_btn_rec != null && right_mover_btn_rec != null && recommendedPosts != null) {
    registerCarouselMovers(recommendedPosts, left_mover_btn_rec, right_mover_btn_rec);
}

function registerCarouselMovers(carouselPosts, left_mover_btn, right_mover_btn) {
    let carousel_pages = Math.ceil(carouselPosts.length / 4);
    let l = 0;
    let movePer = 25.34;
    let maxMove = 203;
// mobile_view
    let mob_view = window.matchMedia("(max-width: 768px)");
    if (mob_view.matches) {
        movePer = 50.36;
        maxMove = 504;
    }

    let right_mover = () => {
        l = l + movePer;
        if (carouselPosts.length === 1) {
            l = 0;
        }
        for (const i of carouselPosts) {
            if (l >= maxMove) {
                l = l - movePer;
            } else {
                i.style.left = '-' + l + '%';
            }
        }

    }

    let left_mover = () => {
        l = l - movePer;
        if (l <= 0) {
            l = 0;
        }
        for (const i of carouselPosts) {
            if (carousel_pages > 1) {
                i.style.left = '-' + l + '%';
            }
        }
    }

    right_mover_btn.onclick = () => {
        right_mover();
    }
    left_mover_btn.onclick = () => {
        left_mover();
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


for (let card of cards) {
    let cardId = card.getAttribute('data-el_id');
    let cardSaveBtn = card.getElementsByClassName('card__save_btn');
    let cardLikeBtn = card.getElementsByClassName('card__like_btn');
    for (let cardSaveBtnElement of cardSaveBtn) {
        cardSaveBtnElement.onclick = () => {
            let saved = cardSaveBtnElement.getAttribute('data-el_saved');
            if (saved == "true") {
                updateSave(cardId, false);
                cardSaveBtnElement.setAttribute('data-el_saved', 'false');
            } else {
                updateSave(cardId, true);
                cardSaveBtnElement.setAttribute('data-el_saved', 'true');
            }
        }
    }
    for (let cardLikeBtnElement of cardLikeBtn) {
        cardLikeBtnElement.onclick = () => {
            let liked = cardLikeBtnElement.getAttribute('data-el_liked');
            if (liked === "true") {
                updateLikes(cardId, false);
                cardLikeBtnElement.setAttribute('data-el_liked', 'false');
            } else {
                updateLikes(cardId, true);
                cardLikeBtnElement.setAttribute('data-el_liked', 'true');
            }
        }
    }

}

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
// let simplemde = new SimpleMDE({ element: document.getElementById("content-editor") });