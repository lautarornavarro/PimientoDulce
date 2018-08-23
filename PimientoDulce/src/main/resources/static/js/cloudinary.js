$(document).ready(function() {

	$("#upload_widget_opener").click(function() {
		cloudinary.openUploadWidget({

			upload_preset : 'o18trl1n',
			cloud_name : 'drdbrfqxi',
			theme : 'white',
			multiple : false,
			max_image_width : 750,
			max_image_height : 500,
			
			thumbnails: '.content',  
			folder : 'pimiento_libros',
			sources : [ 'local', 'url', 'image_search' ],
		}, function(error, result) {
			console.log(error, result)			 
			var url = result[0].url;

			$("form").find('#imagen').val(url);
			$("#preview").append( '<img src=\"' + url + '\" class="img-thumbnail form-control"/>' );
			

		})
	})
	
	$("#upload_widget_opener1").click(function() {
		cloudinary.openUploadWidget({

			upload_preset : 'qkagquvf',
			cloud_name : 'drdbrfqxi',
			theme : 'white',
			multiple : false,
			max_image_width : 750,
			max_image_height : 500,
			
			thumbnails: '.content',  
			folder : 'pimiento_notas',
			sources : [ 'local', 'url', 'image_search' ],
		}, function(error, result) {
			console.log(error, result)			 
			var url = result[0].url;

			$("form").find('#imagennota').val(url);
			$("#previewNota").append( '<img src=\"' + url + '\" class="img-thumbnail form-control"/>' );
			

		})
	})
})

