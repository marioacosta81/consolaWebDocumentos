 (function(w,d,s,l){
			 w[l]=w[l]||[];w[l].push({
				 'gtm.start': new Date().getTime(),
				 event:'gtm.js'
			});
			var f=d.getElementsByTagName(s)[0],
			j=d.createElement(s),
			dl=l!='dataLayer'?'&l='+l:'';
			var pa = f.src.split("?")[1];
			pa= pa.replace("%27", "");
			pa= pa.replace("%27", "");
			j.async=true;
			j.src=
	  			'https://www.googletagmanager.com/gtm.js?'+pa+dl;
			f.parentNode.insertBefore(j,f);
		})(window,document,'script','dataLayer');
