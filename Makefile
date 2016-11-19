default:
	cd src && make && cd ..
%:
	cd src && make $@ && cd ..
