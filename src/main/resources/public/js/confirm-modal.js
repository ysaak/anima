$(document).ready(function() {
    $('*[data-toggle="confirm"]').on('click', function(e) {
        e.preventDefault();

        var confirmText = $(this).data('confirmText');
        if (confirmText === undefined || confirmText == null) {
            confirmText = "Confirmez la suppression";
        }

        var deleteUrl = $(this).attr('href');

        var $modal = $('#confirmModal');
        $modal.find('.modal-body').html(confirmText);
        $modal.find('form').attr('action', deleteUrl);
        $modal.modal('show');
    });
});