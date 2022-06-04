import {postData, patchData, deleteData} from "./ajax.mjs";
import Toast from "./toast.mjs";

function enableFeedbackFunctionality() {

    let feedbackBtn = document.getElementById("feedback-btn");
    feedbackBtn && feedbackBtn.addEventListener('click', leaveFeedback);

    let editBtns = document.querySelectorAll('[data-feedback-edit]');
    for (let editBtn of editBtns) {
        editBtn.onclick = () => {
            let target = editBtn.getAttribute('data-feedback-edit');
            let comment = document.querySelector('[data-feedback-edit-target="' + target + '"]');
            if (comment == null) return;

            comment.setAttribute("contenteditable", "true");
            comment.focus();

            let enterKeyFunc = function (event) {
                // If the user presses the "Enter" key on the keyboard
                if (event.key === "Enter") {
                    // Cancel the default action, if needed
                    event.preventDefault();
                    // Trigger the button element with a click
                    comment.setAttribute("contenteditable", "false");
                    saveComment(comment.textContent, target);
                    comment.removeEventListener('keypress', enterKeyFunc);
                    comment.removeEventListener('focusout', focusLostFunc);
                }
            }

            let focusLostFunc = function () {
                comment.setAttribute("contenteditable", "false");
                saveComment(comment.textContent, target);
                comment.removeEventListener('keypress', enterKeyFunc);
                comment.removeEventListener('focusout', focusLostFunc);
            }

            comment.addEventListener("keypress", enterKeyFunc);
            comment.addEventListener("focusout", focusLostFunc);
        }
    }
    let deleteBtns = document.querySelectorAll('[data-feedback-delete-id]');
    for (let deleteBtn of deleteBtns) {
        deleteBtn.onclick = () => {
            const postId = document.querySelector('meta[name="_post_id"]').content;
            const commentId = deleteBtn.getAttribute('data-feedback-delete-id');

            let confirmed = confirm("Are you sure you want to delete this comment? This action cannot be undone!");

            if (!confirmed) return;

            const cb = function (status) {
                    if (status === 200) {
                        Toast("Feedback deleted successfully", "fa-circle-check");
                        location.reload();
                    } else if (status === 401) {
                        Toast("You are not authorized to do this", "fa-circle-exclamation");
                    } else {
                        Toast("Error: "+status+" Please try again later.", "fa-circle-exclamation");
                    }
            }

            deleteData("/api/post/"+postId+"/comment/"+commentId, cb, "");
        }
    }

    const feedbackContainers = document.getElementsByClassName('feedback-container');
    for (let feedbackContainer of feedbackContainers) {
        let collapsible = feedbackContainer.getElementsByClassName('collapse');
        let btns = feedbackContainer.getElementsByClassName('user__widget_btn');
        for (let collapsibleElement of collapsible) {
            collapsibleElement.addEventListener('show.bs.collapse', function () {
                for (let btn1 of btns) {
                    btn1.classList.add('active');
                }
            });
            collapsibleElement.addEventListener('hide.bs.collapse', function () {
                for (let btn1 of btns) {
                    btn1.classList.remove('active');
                }
            })
        }

    }
}

function saveComment(comment, id) {
    const postId = document.querySelector('meta[name="_post_id"]').content;

    let cb = function (status) {
            if (status === 200) {
                Toast("Comment saved successfully", "fa-circle-check");
            } else {
                Toast("Failed to save comment. Try again later.", "fa-circle-exclamation");
            }
    }
    patchData("/api/post/"+postId+"/comment/"+id, cb, comment);
}

function leaveFeedback() {
    const postId = document.querySelector('meta[name="_post_id"]').content;

    let feedback = document.getElementById('feedback-input');

    if (feedback.value === "" || feedback.value.length < 12) {
        Toast("Feedback must be min 12 characters long", "fa-circle-exclamation");
        return;
    }

    let cb = (status) => {
            if (status === 200) {
                Toast("Successfully posted feedback", "fa-circle-check");
                window.location.reload();
            } else {
                Toast("Error: "+status+" Failed to post feedback", "fa-circle-exclamation");
            }
    }

    postData("/api/post/"+postId+"/comment", cb, feedback.value);
}

export {
    enableFeedbackFunctionality
}