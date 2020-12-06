function check1(line)
    m = match(r"(?<lo>\d+)-(?<hi>\d+) (?<char>\w): (?<pwd>\w*)",line)
    if m === nothing
        false
    else
        parse(Int, m[:lo]) <= count(m[:char],m[:pwd]) <= parse(Int, m[:hi])
    end
end

function check2(line)
    m = match(r"(?<lo>\d+)-(?<hi>\d+) (?<char>\w): (?<pwd>\w*)",line)
    if m === nothing
        false
    else
        xor(m[:pwd][parse(Int, m[:lo])] == m[:char][1],
            m[:pwd][parse(Int, m[:hi])] == m[:char][1])
    end
end

function dec02_task1(pwinput)
    sum((check1(l) for l in split(pwinput,"\n")))
end

function dec02_task2(pwinput)
    sum((check2(l) for l in split(pwinput,"\n")))
end

inputdata = read(joinpath(@__DIR__,"input.txt"),String);

using Test
@test dec02_task1(inputdata) == 586
@test dec02_task2(inputdata) == 352
