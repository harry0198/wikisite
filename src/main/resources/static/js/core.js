// Core javascript files
// Used nearly on all pages


// Navbar logic
let menu = document.querySelector('#menu-bars');
let navbar = document.querySelector('.navbar');
let search = document.querySelector('#search');

menu.onclick = () => {
    menu.classList.toggle('fa-times');
    navbar.classList.toggle('active');
}

search.onclick = () => {
    window.location.href = "https://www.vesudatutorials.com/search";
}

const openModalButtons = document.querySelectorAll("[data-modal-target]");
const closeModalButtons = document.querySelectorAll("[data-close-button]");
const overlay = document.getElementById('overlay');

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
//
//
// // Sign in / Sign up ui logic
//
// let loginContainerBg = document.querySelector(".login-bg-tint");
// let exitBtn = document.querySelector(".login-exit-menu-btn");
// let signInBtn = document.querySelector(".sign-in-btn");
//
// // When clicking outside the modal popup
// loginContainerBg.onclick = (e) => {
//     if(e.target !== e.currentTarget) return;
//     if (exitBtn != null) {
//         closeSignInWindow()
//     }
// }
//
//
//
// // When exit button is struck
// if (exitBtn != null) {
//     exitBtn.onclick = () => {
//         closeSignInWindow()
//     }
// }
//
// if (signInBtn != null) {
//     signInBtn.onclick = () => {
//         loginContainerBg.style.display = 'flex';
//     }
// }
//
// function closeSignInWindow() {
//     loginContainerBg.style.display = 'none';
// }