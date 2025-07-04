//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.10
//
// <auto-generated>
//
// Generated from file `Contract.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package Contract;

/**
 * Helper class for marshaling/unmarshaling CandidateSeq.
 **/
public final class CandidateSeqHelper
{
    public static void write(com.zeroc.Ice.OutputStream ostr, Candidate[] v)
    {
        if(v == null)
        {
            ostr.writeSize(0);
        }
        else
        {
            ostr.writeSize(v.length);
            for(int i0 = 0; i0 < v.length; i0++)
            {
                Candidate.ice_write(ostr, v[i0]);
            }
        }
    }

    public static Candidate[] read(com.zeroc.Ice.InputStream istr)
    {
        final Candidate[] v;
        final int len0 = istr.readAndCheckSeqSize(2);
        v = new Candidate[len0];
        for(int i0 = 0; i0 < len0; i0++)
        {
            v[i0] = Candidate.ice_read(istr);
        }
        return v;
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<Candidate[]> v)
    {
        if(v != null && v.isPresent())
        {
            write(ostr, tag, v.get());
        }
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, Candidate[] v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            CandidateSeqHelper.write(ostr, v);
            ostr.endSize(pos);
        }
    }

    public static java.util.Optional<Candidate[]> read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            Candidate[] v;
            v = CandidateSeqHelper.read(istr);
            return java.util.Optional.of(v);
        }
        else
        {
            return java.util.Optional.empty();
        }
    }
}
