using Pkg
Pkg.activate(@__DIR__)
using PaddedViews

td1 = readlines("testinput1.txt")

function translate_char_val()
    Dict('L' => 0,
    '#' => 1,
    '.' => -1)
end

function translate_val_char()
    translate = translate_char_val()
    Dict(zip(values(translate),keys(translate)))
end

function preparedata(data)
    translate = translate_char_val()
    m = hcat([[translate[c] for c in l] for l in data]...)   
    (X,Y) = size(m)
    return m, PaddedView(-2, m, (0:X+1,0:Y+1))
end

function padded(data)
    translate = translate_char_val()
    m = hcat([[translate[c] for c in l] for l in data]...)   
    padded(m)
end

function padded(m::Matrix)
    (X,Y) = size(m)
    PaddedView(-2, m, (-(X+Y+2):2*(X+Y+1),-(X+Y+2):2*(X+Y+1)))
end



m1,p1 = preparedata(td1)
p1 = padded(td1) 

function occupiedneighbours(thismap, x, y)
    xs = range(-1, step=1, stop=1)
    ys = range(-1, step=1, stop=1)
    res = thismap[x .+ xs,y .+ ys]
    res = vcat(res[1:4],res[6:end])
    sum(res[res .>0])
end

function next_seat(pv, x, y)
    occ_neigh = occupiedneighbours(pv, x, y)
    if pv[x, y] == 0 && occ_neigh == 0
        return 1 #'#'
    elseif pv[x, y] == 1 && occ_neigh >= 4
        return 0 #'L'
    else
        return pv[x,y] #'.'
    end 
end

function next_map(m)
    (X,Y) = size(m)
    nm = copy(m)
    pv = PaddedView(-2, m, (0:X+1,0:Y+1))
    for x in 1:X, y in 1:Y
        nm[x, y] = next_seat(pv, x, y)
    end
    return nm
end


function game_of_seats(m)
    nm = next_map(m)
    if nm == m
        return sum(nm[nm .> 0])
    else
        game_of_seats(nm)
    end
end

function dec11_1(input)
    qi,pv = preparedata(input)
    # println(qi)
    game_of_seats(qi)
end

using Test

quizinput = readlines("input.txt")
@test dec11_1(td1) == 37
@test dec11_1(quizinput) == 2204



# Part II

function next_seat2(pv, x, y)
    occ_neigh = occupiedseen(pv, x, y)
    if pv[x, y] == 0 && occ_neigh == 0
        return 1 #'#'
    elseif pv[x, y] == 1 && occ_neigh >= 5
        return 0 #'L'
    else
        return pv[x,y] #'.'
    end 
end

function occupiedseen(pv,x,y)
    dirs = ((i=>j) for i in (-1:1:1), j in (-1:1:1) if !(i==0 && j==0))
    s = sum((sees_occupied(diagonal(pv,x,y,d.first,d.second)) for d in dirs))
end

function sees_occupied(v) 
    if length(v) > 0
        found = findfirst(x->(x>=0), v)
        if found === nothing
            return 0
        else
            res = v[found]
        end 
    else
        res = 0
    end
    return res
end

function diagonal(pv,x,y,east,south)
    X,Y = size(parent(pv))
    MAX = max(X,Y)
    diag = [pv[x + i* east, y + i*south] for i = 1:MAX]
    # diag = (pv[x + i* east, y + i*south] for i = 1:MAX)
end

function next_map2(m)
    (X,Y) = size(parent(m))
    nm = copy(parent(m))
    for x in 1:X, y in 1:Y
        nm[x, y] = next_seat2(m, x, y)
    end
    return padded(nm)
end

function string_map(m)
    translate = translate_val_char()
    sm = [translate[v] for v in m]
    join((join(sm[:,i],"") for i in 1:size(sm,2)),"\n")
end

function show_map(m)
    println(string_map(parent(m)))
    println( "\n---")
end

function game_of_seats2(m)
    nm = next_map2(m)
    if nm == m
        return nm
    else
        game_of_seats2(nm)
    end
end

function dec11_2(input)
    r = game_of_seats2(padded(input))
    return sum(r[r .> 0])
end

# Tests
t1 = readlines("testinput1.txt")
m2 = next_map2(padded(t1))
t2 = read("testinput2.txt",String)
@test t2 == string_map(parent(m2))

t3 = read("testinput3.txt",String)
m3 = next_map2(m2)
@test t3 == string_map(parent(m3))

@test dec11_2(td1) == 26
@test dec11_2(quizinput) == 1986