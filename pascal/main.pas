program main;
uses
	Sysutils,
	Compressed,
	Crt,
	Dateutils;

var
	FromTime, ToTime : TDateTime;
	tmp : Compress;
	Diff : Integer;
	tfOut : TextFile;
	i, j : Integer;
	tmpFile, fileName : String;

begin
	if (ParamStr(2) = '1') then
	begin
		FromTime := Now;
		tmp := doCompress(ParamStr(1));
		ToTime := Now;
		tmpFile := ParamStr(1);
		i := Length(tmpFile);
		while (tmpFile[i] <> '.') do
		begin
			i := i-1;
		end;
		fileName := '';
		for j := 1 to i-1 do
		begin
			fileName := fileName + tmpFile[j];
		end;
		fileName := fileName + '.irk';
		AssignFile(tfOut, fileName);
		Rewrite(tfOut);
		WriteLn(tfOut, tmp.toBytes());
		WriteLn('Ukuran hasil kompresi adalah: ',SizeOf(tmp),' byte(s)');
		Diff := MilliSecondsBetween(ToTime,FromTime);
		WriteLn('Eksekusi waktu kompresi adalah: ',round(Diff)/1000.0:0:3,' second(s)');
	end
	else
	begin
		FromTime := Now;
		decompress(ParamStr(1));
		ToTime := Now;
		Diff := MilliSecondsBetween(ToTime,FromTime);
		WriteLn('Eksekusi waktu de-kompresi adalah: ',round(Diff)/1000.0:0:3,' second(s)');
	end;
end.