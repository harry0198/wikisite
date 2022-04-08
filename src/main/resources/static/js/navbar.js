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