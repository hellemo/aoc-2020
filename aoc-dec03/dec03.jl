using SparseArrays

function preparemap(thismap)
    splitmap = split(thismap)
    J = length(splitmap)
    I = length(first(splitmap))
    sparse(reshape(vcat([[c=='#' for c in l] for l in split(thismap)]...),(I,J)))
end

function is_tree(preparedmap, pos::Pair)
    is_tree(preparedmap, pos.first, pos.second)
end

function is_tree(preparedmap, x, y)
    X,Y = size(preparedmap)
    preparedmap[mod1(x,X), y]
end

function next_pos(pos, delta)
    Pair(pos.first + delta.first, pos.second + delta.second)
end

function traverse(thismap, start, next_pos)
    X,Y = size(thismap)
    num_trees = is_tree(thismap, start) ? 1 : 0
    if start.second < Y
        num_trees + traverse(thismap, next_pos(start), next_pos)
    else
        num_trees
    end
end

# Part I
function dec03_part1(preparedmap)
    traverse(preparedmap, (1=>1), pos->next_pos(pos,(3=>1)))
end

# Part II
function dec03_part2(preparedmap)
    slopes = ((1=>1), (3=>1), (5=>1), (7=>1), (1=>2))
    prod((traverse(p, (1=>1), pos->next_pos(pos,s)) for s in slopes))
end

function dec03_both(preparedmap, slopes)
    prod((traverse(p, (1=>1), pos->next_pos(pos,s)) for s in slopes))
end

inputdata = read(joinpath(@__DIR__,"input.txt"),String);
p = preparemap(inputdata);
slopes = ((1=>1), (3=>1), (5=>1), (7=>1), (1=>2));

using Test
@test dec03_part1(p) == 299
@test dec03_part2(p) == 3621285278
@test dec03_both(p, [slopes[2]]) == 299
@test dec03_both(p, slopes) == 3621285278