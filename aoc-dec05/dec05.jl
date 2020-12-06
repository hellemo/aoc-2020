function rowfromstring(s)
    parse(Int, replace(replace(s,"B"=>"1"),"F"=>"0");base=2)
end

function colfromstring(s)
    parse(Int, replace(replace(s,"R"=>"1"),"L"=>"0");base=2)
end

function seat(s)
    r = rowfromstring(s[1:7])
    c = colfromstring(s[8:10]) 
    s = r * 8 + c
    return (row=r,col=c,seatid=s)
end
rowfromstring("BBB")
colfromstring("RLL")


# BFFFBBFRRR: row 70, column 7, seat ID 567.
# FFFBBBFRRR: row 14, column 7, seat ID 119.
# BBFFBBFRLL: row 102, column 4, seat ID 820.

spec_1 = ["BFFFBBFRRR","FFFBBBFRRR","BBFFBBFRLL"]
using Test

seat(spec_1[1])

@test seat(spec_1[1]) == (row = 70,col = 7, seatid = 567)
@test seat(spec_1[2]) == (row=14, col=7, seatid=119)
@test seat(spec_1[3]) == (row=102,col=4,seatid=820)

function seatid(s)
    seat(s).seatid
end

maximum(seatid.(spec_1))
testdata = read(joinpath(@__DIR__,"input.txt"),String)

function dec05_1(s)
    maximum(seatid.(split(s)))
end

@test dec05_1(testdata) == 842

function dec05_2(s)
    list = sort(seatid.(split(s)))
    for (idx,v) in enumerate(list[2:end-1])
        if (list[idx] != v - 1 ) || (list[idx+2] != v + 1)
            @show list[idx] v list[idx+2]
            if list[idx] != v-1
                return v
            else
                return v + 1
            end
        end
    end
end

@test dec05_2(testdata) == 617
