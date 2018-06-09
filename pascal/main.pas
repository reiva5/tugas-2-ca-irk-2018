program main;
uses
	Compressed,
	Crt,
	Dateutils,
	Sysutils;

var
	FromTime, ToTime : TDateTime;
	tmp : Compress;
	Diff : Integer;

begin
	if (ParamStr(2) = '1') then
	begin
		FromTime := Now;
		tmp := doCompress(ParamStr(1));
		ToTime := Now;
		WriteLn('Ukuran hasil kompresi adalah: ',SizeOf(tmp),' byte(s)');
		Diff := MilliSecondsBetween(ToTime,FromTime);
		WriteLn('Eksekusi waktu kompresi adalah: ',real(Diff)/1000.0:0:3,' second(s)');
	end
	else
	begin
		FromTime := Now;
		decompress(ParamStr(1));
		ToTime := Now;
		Diff := MilliSecondsBetween(ToTime,FromTime);
		WriteLn('Eksekusi waktu de-kompresi adalah: ',real(Diff)/1000.0:0:3,' second(s)');
	end;
end.