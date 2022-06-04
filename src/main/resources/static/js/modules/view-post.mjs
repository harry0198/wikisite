import {postData, patchData, deleteData} from "./ajax.mjs";

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
                }
            }

            let focusLostFunc = function () {
                comment.setAttribute("contenteditable", "false");
                saveComment(comment.textContent, target);
                comment.removeEventListener('keypress', focusLostFunc);
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
                        location.reload();
                    } else if (status === 401) {
                        alert("You are not authorized to delete this comment!");
                    } else if (status === 404) {
                        alert("Could not find comment to delete! Has it already been deleted?");
                    } else {
                        alert("Could not delete comment. Status code: " + status);
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
                //todo
            }
    }
    patchData("/api/post/"+postId+"/comment/"+id, cb, comment);
}

function leaveFeedback() {
    const postId = document.querySelector('meta[name="_post_id"]').content;

    let feedback = document.getElementById('feedback-input');

    // if (feedback.textContent === "" || feedback.textContent.length < 12) {
    //     let feedbackInvalid = document.getElementById('feedback-invalid');
    //     feedbackInvalid.textContent = "Please provide descriptive feedback! Min 12 characters";
    //     feedbackInvalid.classList.remove('hidden');
    //     return;
    // }

    let cb = (status) => {
            let feedbackInvalid = document.getElementById('feedback-invalid');
            if (status === 200) {
                window.location.reload();
            } else if (status === 404) {
                feedbackInvalid.textContent = "Sorry, we're unable to post your comment! Please try again later.";
                feedbackInvalid.classList.remove('hidden');
            } else {
                feedbackInvalid.textContent = "Unknown Error! Status code: " + status;
                feedbackInvalid.classList.remove('hidden');
            }
    }

    postData("/api/post/"+postId+"/comment", cb, feedback.value);
}

export {
    enableFeedbackFunctionality
}