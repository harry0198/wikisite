
function enableDragNDropFunctionality() {
    let dragndrops = document.getElementsByClassName('file-input');
    let dragndropcontainers = document.getElementsByClassName('file-container')

    let addClass2Container = function() {
        for (let dragndropcontainer of dragndropcontainers) {
            dragndropcontainer.classList.add("drag-active");
        }
    }
    let removeClass2Container = function() {
        for (let dragndropcontainer of dragndropcontainers) {
            dragndropcontainer.classList.remove("drag-active");
        }
    }

    for (let dragndrop of dragndrops) {
        dragndrop.addEventListener("dragenter", event => {
            // highlight potential drop target when the draggable element enters it
            addClass2Container()
        });

        dragndrop.addEventListener("dragleave", event => {
            // reset background of potential drop target when the draggable element leaves it
            removeClass2Container()
        });

        dragndrop.addEventListener("mouseout", event => {
            // reset background of potential drop target when the draggable element leaves it
            removeClass2Container()
        });
    }
}



function stopEnterKeyFormSubmission() {
    let cancelEnterSubmitInput = document.getElementsByClassName('no-submit');
    for (let el of cancelEnterSubmitInput) {
        el.addEventListener("keypress", function(event) {
            if (event.key === "Enter") {
                event.preventDefault()
            }
        });
    }
}

export {
    enableDragNDropFunctionality,
    stopEnterKeyFormSubmission
}