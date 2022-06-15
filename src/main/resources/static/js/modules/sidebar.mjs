
let sidebar;

function enableSidebarFunctionality() {
    sidebar = document.getElementById("sidebar");

    if (sidebar == null) return;

    let openBtn = document.getElementById('view-fb-btn');
    if (openBtn != null) {
        openBtn.onclick = (e) => {
            openSidebar();
            e.stopImmediatePropagation();
        }
    }


    sidebar.addEventListener('keyup', e => {
        if (e.code === 'Escape') {
            closeSidebar();
        }
    });

    document.addEventListener("click", act);
}

function act(e) {
    if (sidebar.getAttribute("state") === "open") {
        if (sidebar.contains(e.target)) return;
        closeSidebar();
    }
}

function closeSidebar() {
    sidebar.setAttribute("state", "");
}
function openSidebar() {
    sidebar.setAttribute("state", "open");
}

export {
    enableSidebarFunctionality,
    openSidebar,
    closeSidebar
}