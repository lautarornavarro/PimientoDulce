$(document).ready(function() {

	$("#upload_widget_opener").click(function() {
		cloudinary.openUploadWidget({

			upload_preset : 'o18trl1n',
			cloud_name : 'drdbrfqxi',
		
			folder : 'pimiento_libros',
			sources : [ 'local', 'url', 'image_search' ],
		}, function(error, result) {
			console.log(error, result)			 
			var url = result[0].url;

			$("form").find('#foto').val(url);
			$("#preview1").append( '<img src=\"' + url + '\" class="img-thumbnail form-control"/>' );
			

		})
	})
	
})

