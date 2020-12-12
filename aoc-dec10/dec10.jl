function options_from_n(adapterlist, n)
    filter((p)->((p>n)&&(p<=n+3)),adapterlist)
end

function simple_count(li)
    bestcount = zeros(BigInt,maximum(li))
    sort!(li)
    tovisit = options_from_n(li,0)
    for o in tovisit
        bestcount[o] = 1
    end
    while length(tovisit) > 0
        n = popfirst!(tovisit)
        options = options_from_n(li,n)        
        push!(tovisit,options...)
        sort!(unique!(tovisit))
        for o in options
            bestcount[o] += max(bestcount[n],1)
        end
    end
    return maximum(bestcount)
end

function dec10_2(data)
    simple_count(parse.(Int,data)) 
end

quizinput = readlines("input.txt")

using Test
@test dec10_2(quizinput) == 64793042714624