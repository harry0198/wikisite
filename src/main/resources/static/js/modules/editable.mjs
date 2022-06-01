function applyEditableEventHandlers(editSelector, cb) {
    let editBtns = document.querySelectorAll(editSelector);
    for (let editBtn of editBtns) {
        let targetId = editBtn.getAttribute('data-edit-target');
        let targetElement = document.querySelector(targetId);
        // If provided target element doesnt exist - exit
        if (targetElement == null) return;

        targetElement.setAttribute("contenteditable", "true");
        targetElement.focus();

        let enterKeyFunc = function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
                stopEditBehaviour();
            }
        }

        let focusLostFunc = function(event) {
            stopEditBehaviour();
        }

        let stopEditBehaviour = function() {
            targetElement.setAttribute("contenteditable", "false");
            // Execute callback func
            cb();

            targetElement.removeEventListener("keypress", enterKeyFunc);
            targetElement.removeEventListener("focusout", focusLostFunc);
        }

        targetElement.addEventListener("keypress", enterKeyFunc);
        targetElement.addEventListener("focusout", focusLostFunc);

    }
}

export {init}