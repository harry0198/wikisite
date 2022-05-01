// Core javascript files
// Used nearly on all pages

//
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
let carouselPosts = document.getElementById('article__popular-cards').getElementsByClassName('article__card');
if (left_mover_btn != null && right_mover_btn != null && carouselPosts != null) {
    registerCarouselMovers(carouselPosts, left_mover_btn, right_mover_btn);
}

let left_mover_btn_rec = document.getElementById('article__recommended-move-left');
let right_mover_btn_rec = document.getElementById('article__recommended-move-right');
let recommendedPosts = document.getElementById('article__recommended-cards').getElementsByClassName('article__card');
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