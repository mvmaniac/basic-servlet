String.prototype.format = function() {

    var args = arguments;

    return this.replace(/{(\d+)}/g, function(match, number) {
        return typeof args[number] !== "undefined" ? args[number] : match;
    });
};

$("div.answerWrite input[type=submit]").click(addAnswer);

function addAnswer(e) {

    e.preventDefault();

    var queryString = $("form[name=answer]").serialize();

    $.ajax({
        type: "post",
        url: "/api/qna/addAnswer",
        data: queryString,
        dataType: "json",
        error: onError,
        success: onSuccess
    });

}

function onError(jqXHR, status) {
    alert("error");
}

function onSuccess(json, status) {

    var result = json.result;

    if (result.status) {
        var answer = json.answer;
        var answerTemplate = $("#answerTemplate").html();
        var template = answerTemplate.format(answer.writer, new Date(answer.createdDate), answer.contents, answer.answerId, answer.answerId);
        $("div.qna-comment-slipp-articles").prepend(template);
    } else {
        alert(result.message);
    }
}

$(".qna-comment").on("click", ".form-delete", deleteAnswer);

function deleteAnswer(e) {

    e.preventDefault();

    var deleteBtn = $(this);
    var queryString = deleteBtn.closest("form").serialize();

    $.ajax({
        type: 'post',
        url: "/api/qna/deleteAnswer",
        data: queryString,
        dataType: 'json',
        error: function (xhr, status) {
            alert("error");
        },
        success: function (json, status) {
            if (json.status) {
                deleteBtn.closest('article').remove();
            }
        }
    });
}
