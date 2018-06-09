unit Compressed;

interface

type
	Compress = object
		public
			function toBytes(): PChar;
			{ANOTHER METHOD OR VARIABLE NEEDED}
	end;

function doCompress(namaFile: string) : Compress;
procedure decompress(namaFile : string);

implementation
function Compress.toBytes(): PChar;
begin

end;

function doCompress(namaFile: string) : Compress;
begin

end;

procedure decompress(namaFile : string);
begin

end;
end.