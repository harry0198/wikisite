
function enableNavbarFunctionality() {
// Navbar logic
    let menu = document.querySelector('#menu-bars');
    let navbar = document.querySelector('.navbar');
    let search = document.querySelector('#search');

    if (menu != null) {
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
}
export {
    enableNavbarFunctionality
}