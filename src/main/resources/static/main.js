$(document).ready(function (){
    $('.table .eBtn').on('click',function(event) {
        event.preventDefault();
        var href = $(this).attr('href');
        console.log(href);


        $.get(href, function(user, status) {
            $('.edit#id_editModal').val(user.id);
            $('.edit#name_editModal').val(user.name);
            $('.edit#age_editModal').val(user.age)
            $('.edit#username_editModal').val(user.username);
            $('.edit#password_editModal').val(user.password);
        });

        $('.edit#editModal').modal();
    });

    $('.table .delBtn').on('click', function (event) {
        event.preventDefault();
        let href = $(this).attr('href');
        $('#deleteModal #delRef').attr('href', href);

        $.get(href, function(user, status) {
            $('#delete_form#id_deleteModal').val(user.id);
            $('#delete_form#name_deleteModal').val(user.name);
            $('#delete_form#age_deleteModal').val(user.age)
            $('#delete_form#username_deleteModal').val(user.username);
        });

        $('#deleteModal').modal();
    });

});