unit Compressed;

interface

type
	Compress = object
		public
			function toBytes(): PChar;
			{ YOU CAN ADD ANOTHER METHOD OR VARIABLE BELOW HERE }
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